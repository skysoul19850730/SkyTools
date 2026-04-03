package com.skysoul.accountremebercompose.permission

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.pwrd.dls.marble.common.permission.FAKE_WRITE_PERMISSION
import java.util.function.Consumer
import kotlinx.coroutines.*

class PermissionHandler private constructor(val obj: Any) {
    constructor(activity: Activity) : this(obj = activity)
    constructor(fragment: Fragment) : this(obj = fragment)

    private lateinit var completableDeferred: CompletableDeferred<List<PermissionFail>>
    private val requestCode = 0
    private val activity: Activity? by lazy {
        when (obj) {
            is Activity -> obj
            is Fragment -> obj.activity
            else -> null
        }
    }

    private var assistPopupWindow: PopupWindow? = null
    private var assistWindowParent: View? = null

    /**
     * 为权限使用目的辅助弹窗设置parent
     * 当权限动态申请发生在有某些dialog正在展示时，为了使辅助弹窗能够覆盖在dialog上面，可以通过将dialog的根布局设置为辅助弹窗的parent实现
     */
    fun setAssistWindowParent(parent: View) {
        assistWindowParent = parent
    }

    fun hasPermission(vararg permissions: String): Boolean {
        val deniedPermission = permissions.filter {
            ActivityCompat.checkSelfPermission(activity!!, it) == PackageManager.PERMISSION_DENIED
        }
        return deniedPermission.isEmpty()
    }

    fun requestPermissionInJava(a: Consumer<List<PermissionFail>>, vararg permissions: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val requestPermission = requestPermission(*permissions)
            a.accept(requestPermission ?: emptyList())
        }
    }

    /**
     * @param permissions 请求响应权限
     * @return 没有取得的权限，如果empty，则所有权限已经获，如果为null 则是被打断
     */
    suspend fun requestPermission(vararg permissions: String): List<PermissionFail>? {
        activity ?: return listOf()
        val deniedPermission = permissions.filter {
            it != FAKE_WRITE_PERMISSION
        }.filter {
            ActivityCompat.checkSelfPermission(activity!!, it) == PackageManager.PERMISSION_DENIED
        }
        if (deniedPermission.isEmpty()) {
            return listOf()
        }

        completableDeferred = CompletableDeferred()

        //展示权限使用目的辅助弹窗
//        showAssistPopupWindow(deniedPermission)

        when (obj) {
            is Activity -> ActivityCompat.requestPermissions(obj, deniedPermission.toTypedArray(), requestCode)
            is Fragment -> obj.requestPermissions(deniedPermission.toTypedArray(), requestCode)
        }
        return try {
            completableDeferred.await()
        } catch (e: Exception) {
            null
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode != this.requestCode || !this::completableDeferred.isInitialized) {
            return
        }
        assistPopupWindow?.dismiss()
        assistPopupWindow = null
        if (permissions.isEmpty()) {
            completableDeferred.cancel()
            return
        }
        val deniedList = permissions.filterIndexed { index, _ ->
            grantResults[index] == PackageManager.PERMISSION_DENIED
        }
        val map = deniedList.map {
            val shouldShowRequestPermissionRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity!!, it)
            PermissionFail(it, !shouldShowRequestPermissionRationale)
        }
        completableDeferred.complete(map)
    }
}

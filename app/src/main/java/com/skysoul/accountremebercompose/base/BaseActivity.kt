package com.skysoul.accountremebercompose.base

//import com.google.accompanist.systemuicontroller.rememberSystemUiController
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.skysoul.accountremebercompose.permission.PermissionFail
import com.skysoul.accountremebercompose.permission.PermissionHandler
import com.skysoul.accountremebercompose.ui.theme.AppTheme
import com.skysoul.accountremebercompose.utils.ifValue
import com.skysoul.utils.ToastUtil
import java.lang.reflect.ParameterizedType
import java.util.function.Consumer

/**
 *@author shenqichao
 *Created on 2022/9/1
 *@Description
 */
open abstract class BaseActivity<VM : BaseViewModel> : FragmentActivity() {
    lateinit var viewModel: VM

    val permissionHandler by lazy { PermissionHandler(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        initViewModel()
        doStartObserver()
        setContent {
//            val sys = rememberSystemUiController()
//            sys.setSystemBarsColor(Color.Transparent, darkIcons = !isSystemInDarkTheme())
            AppTheme {
                Box {
                    getPage()
                    if (viewModel.isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center)
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }

    private fun doStartObserver() {
        viewModel.finish.observe(this) {
            if (it)
                doFinish()
        }
        viewModel.toast.observe(this) {
            it.ifValue { s ->
                ToastUtil.showToast(s)
            }
        }
        startObserver()
        initData()
    }

    open fun doFinish() {
        super.finish()
    }

    @Composable
    abstract fun getPage()
    abstract fun initData()

    abstract fun startObserver()

    open fun initViewModel() {
        viewModel = ViewModelProvider(this).get(getVmClazz(this) as Class<VM>)
    }


    private fun <VM> getVmClazz(obj: Any): VM {
        return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
    }
    public override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    fun hasPermission(vararg permissions: String): Boolean {
        return permissionHandler.hasPermission(*permissions)
    }

    fun requestPermission(
        consumer: Consumer<List<PermissionFail>>,
        vararg permissions: String
    ) {
        permissionHandler.requestPermissionInJava(consumer, *permissions)
    }

    fun startToSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", this.getPackageName(), null)
        intent.setData(uri)
        startActivity(intent)
    }
}
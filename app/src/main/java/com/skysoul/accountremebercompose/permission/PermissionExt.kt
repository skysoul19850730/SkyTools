package com.pwrd.dls.marble.common.permission

import android.Manifest
import android.os.Build
import com.skysoul.accountremebercompose.base.BaseActivity
import com.skysoul.accountremebercompose.base.BaseViewModel

const val FAKE_WRITE_PERMISSION = "all_history_fake_write_permission"

/**
 * 简化权限获取
 * 系统权限获取弹窗
 * @param systemPermissions Manifest.permission 里面的常量
 * @param success 系统权限获取后要做的事情
 */
fun BaseActivity<*>.doWithSystemPermission(vararg systemPermissions: String, success: Runnable) {
    if (hasPermission(*systemPermissions)) {
        success.run()
    } else {
        requestSystemPermission(success, *systemPermissions)
    }
}

private fun BaseActivity<*>.requestSystemPermission(success: Runnable?, vararg permissions: String) {
    requestPermission({ permissionFails ->
        when {
            permissionFails == null -> requestSystemPermission(success, *permissions)
            permissionFails.isEmpty() -> success?.run()
            permissionFails[0]!!.neverAsk -> startToSettings()
        }
    }, *permissions)
}


fun readImagePermission(): String =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE

fun readVideoPermission(): String =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_VIDEO else Manifest.permission.READ_EXTERNAL_STORAGE

fun readAudioPermission(): String =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_AUDIO else Manifest.permission.READ_EXTERNAL_STORAGE

fun writePermission(): String =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) FAKE_WRITE_PERMISSION else Manifest.permission.WRITE_EXTERNAL_STORAGE
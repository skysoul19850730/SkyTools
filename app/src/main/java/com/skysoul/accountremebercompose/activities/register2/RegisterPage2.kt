package com.skysoul.accountremebercompose.activities.register2

import android.Manifest
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pwrd.dls.marble.common.permission.doWithSystemPermission
import com.pwrd.dls.marble.common.permission.readImagePermission
import com.skysoul.accountremebercompose.R
import com.skysoul.accountremebercompose.base.BaseActivity
import com.skysoul.accountremebercompose.managers.UserManager
import com.skysoul.accountremebercompose.ui.VSpace
import com.skysoul.accountremebercompose.ui.VSpace16
import com.skysoul.accountremebercompose.ui.bar.backOverBar
import com.skysoul.accountremebercompose.ui.bar.topBarPage
import com.skysoul.accountremebercompose.ui.button
import com.skysoul.accountremebercompose.ui.edittext.EditText
import com.skysoul.accountremebercompose.ui.edittext.PasswordEditText
import com.skysoul.album.AlbumManager
import com.skysoul.album.OnSelectedCallback
import com.skysoul.album.bean.MediaInfo
import com.skysoul.album.bean.MediaType

/**
 *@author shenqichao
 *Created on 2022/8/31
 *@Description
 */

@Composable
fun registerPage2(viewModel: RegisterViewModel2 = viewModel()) {

    val activity = (LocalActivity.current as? BaseActivity<*>) ?: return

    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    topBarPage(
        topbar = {
            backOverBar(
                title = "完善信息",
                onBackPressed = {
                    viewModel.finish.value = true
                },
                onOver = { viewModel.doOver(imageBitmap) }
            )
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VSpace(50)
            val painter = if (imageBitmap != null) BitmapPainter(imageBitmap!!.asImageBitmap())
            else painterResource(R.mipmap.add_icon_red)
            Image(
                painter = painter, contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clickable {
                        activity.doWithSystemPermission(readImagePermission()) {
                            AlbumManager.doSelected()
                                .setMediaType(MediaType.IMAGE)
                                .setOpenCropDirectly(true)
                                .setCropRatio(1f)
                                .setMaxSelectImageCount(1)
                                .setImageLimitSize(100 * 1024)
                                .setOnSelectedCallback(object : OnSelectedCallback {
                                    override fun onSuccess(p0: List<MediaInfo>) {
                                        val imagePath = p0.getOrNull(0)?.compressPath ?: ""
                                        if (imagePath.isNotEmpty()) {
                                            imageBitmap = BitmapFactory.decodeFile(imagePath)
                                        }
                                    }

                                    override fun onFail(p0: Int, p1: String?) {
                                    }
                                }).start(activity)
                        }
                    })

            VSpace16()
            Text("头像")
            VSpace(32)
            EditText(state = viewModel.userNameState, label = "昵称")

            if (!viewModel.usePassLogin) {
                VSpace16()
                PasswordEditText(state = viewModel.passwordState, label = "查看密码")
            }
            VSpace16()
            Row() {
                Text(
                    text = "使用账户密码作为账号查看密码", modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.Start)
                )
                Switch(checked = viewModel.usePassLogin, onCheckedChange = {
                    viewModel.usePassLogin = it
                })
            }

            VSpace16()



            if (viewModel.di2 != null) {
                AlertDialog(
                    onDismissRequest = {}, properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                ), text = {
                    Text(text = "没有设置查看密码，也没有开启使用登录密码，查看账号时，不会有密码验证")
                }, confirmButton = {
                    TextButton(onClick = { viewModel.di2?.invoke(true) }) {
                        Text(text = "确定")
                    }
                }, dismissButton = {
                    TextButton(onClick = { viewModel.di2?.invoke(false) }) {
                        Text(text = "取消")
                    }
                })
//                {
//                    Column {
//                        Text(text = "没有设置查看密码，也没有开启使用登录密码，查看账号时，不会有密码验证")
//                        Row {
//                            Text(text = "取消", modifier = Modifier.clickable {
//                                viewModel.di2?.invoke(false)
//                            })
//                            Text(text = "确定", modifier = Modifier.clickable {
//                                viewModel.di2?.invoke(true)
//                            })
//                        }
//                    }
//                }
            }

        }

        button("退出", Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp).fillMaxWidth().align(Alignment.BottomCenter)) {
            UserManager.logout(activity)
        }
    }

}
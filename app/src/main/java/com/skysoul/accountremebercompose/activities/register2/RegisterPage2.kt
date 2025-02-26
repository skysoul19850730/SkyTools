package com.skysoul.accountremebercompose.activities.register2

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skysoul.accountremebercompose.ui.VSpace16
import com.skysoul.accountremebercompose.ui.bar.backOverBar
import com.skysoul.accountremebercompose.ui.bar.topBarPage
import com.skysoul.accountremebercompose.ui.edittext.EditText
import com.skysoul.accountremebercompose.ui.edittext.PasswordEditText

/**
 *@author shenqichao
 *Created on 2022/8/31
 *@Description
 */

@Composable
fun registerPage2(viewModel: RegisterViewModel2 = viewModel()) {


    topBarPage(
        topbar = {
            backOverBar(
                title = "完善信息",
                onBackPressed = {
                    viewModel.finish.value = true
                },
                onOver = { viewModel.doOver() }
            )
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {

            VSpace16()
            EditText(state = viewModel.userNameState, label = "昵称")
            if (!viewModel.usePassLogin) {
                VSpace16()
                PasswordEditText(state = viewModel.passwordState, label = "查看密码")
            }
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

            if (viewModel.di2 != null) {
                AlertDialog(onDismissRequest = {}, properties = DialogProperties(
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
    }

}
package com.skysoul.accountremebercompose.activities.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skysoul.accountremebercompose.ui.VSpace16
import com.skysoul.accountremebercompose.ui.bar.backPage
import com.skysoul.accountremebercompose.ui.edittext.EditText
import com.skysoul.accountremebercompose.ui.edittext.PasswordEditText

/**
 *@author shenqichao
 *Created on 2022/8/31
 *@Description
 */

@Composable
fun registerPage(viewModel: RegisterViewModel = viewModel()) {

    backPage(title = "注册", onBackPressed = { viewModel.finish.value = true }) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {

            VSpace16()
            EditText(state = viewModel.userNameState, label = "用户名")
            VSpace16()
            PasswordEditText(state = viewModel.passwordState, label = "密码")
            VSpace16()
            PasswordEditText(state = viewModel.passwordAgainState, label = "密码确认")
            VSpace16()
            PasswordEditText(state = viewModel.passwordTipState, label = "密码提示")
            VSpace16()
            Button(
                onClick = {
                    viewModel.register()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                enabled = viewModel.userNameState.enable
                        && viewModel.passwordState.enable
                        && viewModel.passwordAgainState.enable
            ) {
                Text(
                    text = "注册"
                )
            }
        }
    }

}
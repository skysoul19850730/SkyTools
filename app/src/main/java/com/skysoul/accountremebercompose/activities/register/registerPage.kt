package com.skysoul.accountremebercompose.activities.register

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skysoul.accountremebercompose.ui.VSpace16
import com.skysoul.accountremebercompose.ui.bar.backPage
import com.skysoul.accountremebercompose.ui.button
import com.skysoul.accountremebercompose.ui.edittext.EditText
import com.skysoul.accountremebercompose.ui.edittext.PasswordEditText
import com.skysoul.accountremebercompose.ui.edittext.TextFieldState
import com.skysoul.accountremebercompose.ui.theme.AppTheme

/**
 *@author shenqichao
 *Created on 2022/8/31
 *@Description
 */

@Composable
fun RegisterPage(viewModel: RegisterViewModel = viewModel()) {

    RegisterContent(
        viewModel.userNameState,
        viewModel.passwordState,
        viewModel.passwordAgainState,
        viewModel.passwordTipState,
        backClick = { viewModel.finish.value = true },
        register = {
            viewModel.register()
        }
    )
}

@Composable
fun RegisterContent(
    userNameState: TextFieldState,
    passwordState: TextFieldState,
    passwordAgainState: TextFieldState,
    passwordTipState: TextFieldState,
    backClick: () -> Unit,
    register: () -> Unit
) {
    backPage(title = "注册", onBackPressed = { backClick.invoke() }) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {

            VSpace16()
            EditText(state = userNameState, label = "用户名")
            VSpace16()
            PasswordEditText(state = passwordState, label = "密码")
            VSpace16()
            PasswordEditText(state = passwordAgainState, label = "密码确认")
            VSpace16()
            PasswordEditText(state = passwordTipState, label = "密码提示")
            VSpace16()

            button(
                "注册", modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                enabled = userNameState.enable
                        && passwordState.enable
                        && passwordAgainState.enable
            ) {
                register.invoke()
            }

        }
    }
}

@Composable
@Preview("TestTwo")
@Preview("TestTwo", uiMode = Configuration.UI_MODE_NIGHT_YES)
fun testTwo() {
    AppTheme {
        RegisterContent(
            TextFieldState(),
            TextFieldState(),
            TextFieldState(),
            TextFieldState(),
            {},
            {})
    }
}
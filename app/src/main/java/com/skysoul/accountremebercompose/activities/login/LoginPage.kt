package com.skysoul.accountremebercompose.activities.login

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skysoul.accountremebercompose.ui.VSpace16
import com.skysoul.accountremebercompose.ui.button
import com.skysoul.accountremebercompose.ui.edittext.EditText
import com.skysoul.accountremebercompose.ui.edittext.PasswordEditText
import com.skysoul.accountremebercompose.ui.edittext.TextFieldState
import com.skysoul.accountremebercompose.ui.theme.AppTheme
import com.skysoul.accountremebercompose.ui.vSpace
import com.skysoul.accountremebercompose.utils.toast

/**
 *@author shenqichao
 *Created on 2022/8/29
 *@Description
 */

@Composable
fun loginPage() {

    val viewModel: LoginViewModel = viewModel()

    LoginContent(viewModel.userNameState,
        viewModel.passwordState,viewModel.passTip.value,
        onUsernameChange = {
            viewModel.passTip.value = ""
        }, onPasswordTipClick = {
            viewModel.getTips()
        },
        onLoginClick = {
            viewModel.login()
        }, onRegisterClick = {
            viewModel.registerType.postValue(1)
        }
    )
}

@Composable
fun LoginContent(
    userNameState: TextFieldState, passwordState: TextFieldState, passTip: String,
    onUsernameChange: (String) -> Unit, onPasswordTipClick: () -> Unit,
    onLoginClick: () -> Unit, onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        100.vSpace()
        EditText(state = userNameState, label = "用户名") {
            onUsernameChange(it)
        }
        VSpace16()
        PasswordEditText(state = passwordState, label = "密码")
        VSpace16()

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            AnimatedVisibility(visible = passTip.isNotEmpty(), Modifier.weight(1f)) {
                Text(text = passTip)
            }
            Text(text = "密码提示", Modifier.clickable {
                if(userNameState.text.isEmpty()){
                    toast("请输入用户名")
                }else {
                    onPasswordTipClick.invoke()
                }
            })
        }

        VSpace16()

        button("登录", modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
            enabled = userNameState.errorText.isEmpty()
                    && passwordState.errorText.isEmpty()
                    && userNameState.text.isNotEmpty()
                    && passwordState.text.isNotEmpty()) {
            onLoginClick.invoke()
        }

        Text(
            text = "注册",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable(onClick = {
                    onRegisterClick.invoke()
                }, indication = null, interactionSource = remember { MutableInteractionSource() })
                .padding(50.dp),
            fontSize = 16.sp,
            textDecoration = TextDecoration.Underline,
        )


    }
}

@Composable
@Preview("TestTwo")
@Preview("TestTwo", uiMode = Configuration.UI_MODE_NIGHT_YES)
fun testTwo() {
    AppTheme {
        LoginContent(TextFieldState(), TextFieldState(), "", {}, {}, {}, {})
    }
}
package com.skysoul.accountremebercompose.activities.login

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skysoul.accountremebercompose.ui.VSpace16
import com.skysoul.accountremebercompose.ui.edittext.EditText
import com.skysoul.accountremebercompose.ui.edittext.PasswordEditText
import com.skysoul.accountremebercompose.ui.theme.AppTheme
import com.skysoul.accountremebercompose.ui.vSpace

/**
 *@author shenqichao
 *Created on 2022/8/29
 *@Description
 */

@Composable
fun loginPage(viewModel: LoginViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        100.vSpace()
        EditText(state = viewModel.userNameState, label = "用户名"){
            viewModel.passTip.value = ""
        }
        VSpace16()
        PasswordEditText(state = viewModel.passwordState, label = "密码")
        VSpace16()

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            AnimatedVisibility(visible = viewModel.passTip.value.isNotEmpty(), Modifier.weight(1f)) {
                Text(text = viewModel.passTip.value)
            }
            Text(text = "密码提示",Modifier.clickable {
                viewModel.getTips()
            })
        }

        VSpace16()
        Button(
            onClick = {
                viewModel.login()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            enabled = viewModel.userNameState.errorText.isEmpty()
                    && viewModel.passwordState.errorText.isEmpty()
                    && viewModel.userNameState.text.isNotEmpty()
                    && viewModel.passwordState.text.isNotEmpty()
        ) {
            Text(
                text = "登录"
            )
        }
        Text(
            text = "注册",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable(onClick = {
                    viewModel.registerType.postValue(1)
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
        loginPage()
    }
}
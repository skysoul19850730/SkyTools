package com.skysoul.accountremebercompose.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 *@author shenqichao
 *Created on 2022/8/29
 *@Description
 */

@Composable
fun button(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(onClick = {
        onClick.invoke()
    }, modifier = modifier, enabled = enabled) {
        Text(text)
    }

}

@Composable
fun VSpace(dp: Int) {
    Spacer(modifier = Modifier.height(dp.dp))
}

@Composable
fun HSpace(dp: Int) {
    Spacer(modifier = Modifier.width(dp.dp))
}

@Composable
fun VSpace16() {
    VSpace(dp = 16)
}

@Composable
fun HSpace16() {
    HSpace(dp = 16)
}

@Composable
fun Int.vSpace() {
    VSpace(this)
}

@Composable
fun Int.hSpace() {
    HSpace(this)
}

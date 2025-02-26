package com.skysoul.accountremebercompose.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.unit.dp

/**
 *@author shenqichao
 *Created on 2022/8/29
 *@Description
 */

@Composable
fun VSpace(dp:Int){
    Spacer(modifier = Modifier.height(dp.dp))
}
@Composable
fun HSpace(dp:Int){
    Spacer(modifier = Modifier.width(dp.dp))
}
@Composable
fun VSpace16(){
    VSpace(dp = 16)
}

@Composable
fun HSpace16(){
    HSpace(dp = 16)
}
@Composable
fun Int.vSpace(){
    VSpace(this)
}
@Composable
fun Int.hSpace(){
    HSpace(this)
}

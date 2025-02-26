package com.skysoul.accountremebercompose

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 *@author shenqichao
 *Created on 2022/8/29
 *@Description
 */

fun ViewModel.launch(block: suspend CoroutineScope.() -> Unit):Job{
    return viewModelScope.launch(Dispatchers.IO) {
        block.invoke(this)
    }
}

@Composable
fun ImageBitmap.Companion.fromAssets(name:String):ImageBitmap?{
    val assetManager = LocalContext.current.resources.assets
    return try {
         BitmapFactory.decodeStream(assetManager.open(name)).asImageBitmap()
    }catch (e:Exception){
        null
    }
}

fun log(msg:String){
    Log.d("sqc",msg)
}
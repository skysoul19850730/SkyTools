package com.skysoul.accountremebercompose.utils

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput

/**
 *@author shenqichao
 *Created on 2022/9/22
 *@Description
 */

fun Modifier.gestureClick(key:Any?,onLongClick: ((Offset) -> Unit)? = null,onDoubleClick: ((Offset) -> Unit)? = null,onClick: ((Offset) -> Unit)? = null,): Modifier {
    return pointerInput(key) {
        detectTapGestures(onLongPress = onLongClick, onDoubleTap = onDoubleClick, onTap = onClick)
    }
}
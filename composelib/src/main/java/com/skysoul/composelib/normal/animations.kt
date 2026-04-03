package com.skysoul.composelib.normal

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

/**
 *@author shenqichao
 *Created on 2026/1/9
 *@Description
 */
@Composable
fun Modifier.rotate360(
    trigger: MutableState<Boolean>,
    duration: Int = 300
): Modifier {
    var rotation by remember { mutableStateOf(0f) }
    val animatedRotation by animateFloatAsState(
        targetValue = rotation,
        animationSpec = tween(durationMillis = if(rotation == 0f ) 0 else duration),
        finishedListener = {
            rotation = 0f // 重置为0，准备下次旋转
            trigger.value = false
        }
    )

    LaunchedEffect(trigger.value) {
        if(trigger.value) {
            rotation = 360f // 触发时旋转360度
        }
    }

    return this.graphicsLayer {
        rotationZ = animatedRotation
    }
}

@Composable
fun Modifier.continuousRotate360(
    isRotating: Boolean,
    duration: Int = 2000
): Modifier {
    var rotation by remember { mutableStateOf(0f) }

    LaunchedEffect(isRotating) {
        if (isRotating) {
            // 持续增加旋转角度
            while (true) {
                rotation += 1f
                if (rotation >= 360f) {
                    rotation = 0f
                }
                kotlinx.coroutines.delay(duration.toLong() / 360)  // 每度的延迟
            }
        } else {
            rotation = 0f
        }
    }

    return this.graphicsLayer {
        rotationZ = if (isRotating) rotation else 0f
    }
}
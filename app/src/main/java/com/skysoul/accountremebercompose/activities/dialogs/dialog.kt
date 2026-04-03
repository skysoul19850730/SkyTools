package com.skysoul.accountremebercompose.activities.dialogs

import android.view.ViewGroup
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlinx.coroutines.delay

/**
 *@author shenqichao
 *Created on 2026/2/26
 *@Description
 */

@Composable
fun CustomDialog(
    visible: MutableState<Boolean>,
    defaultBg: Boolean = true,
    defaultAnimTime: Int = 300,
    bgClickDismiss: Boolean = true,
    content: @Composable BoxScope.(animateVisible: MutableState<Boolean>) -> Unit
) {
    val animateVisible = remember { mutableStateOf(false) }
    val animateProcess = animateFloatAsState(
        targetValue = if (animateVisible.value) 1f else 0f,
        animationSpec = tween(defaultAnimTime),
        finishedListener = {
            if (!animateVisible.value) {
                visible.value = false
            }
        }
    )
    LaunchedEffect(visible.value) {
        if (visible.value) {
            delay(50) // 小延迟确保Dialog已渲染
            animateVisible.value = true
        }
    }

    if (!visible.value) return

    Dialog(
        onDismissRequest = {
            animateVisible.value = false
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = true,
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            decorFitsSystemWindows = false
        )
    ) {
        val dialogWindow = (LocalView.current.parent as? DialogWindowProvider)?.window

        // 使用 SideEffect 同步设置窗口属性，避免状态栏隐藏的动画过程
        SideEffect {
            dialogWindow?.let { window ->
                // 禁用 Dialog 的默认动画（使用 0 完全禁用）
                window.setWindowAnimations(0)
                // 去掉半透明黑色背景
                window.setDimAmount(0f)

                // 设置窗口布局参数为全屏
                window.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                WindowCompat.setDecorFitsSystemWindows(window, false)
                WindowCompat.getInsetsController(window, window.decorView)
                    .apply {
                        hide(WindowInsetsCompat.Type.statusBars())
                        systemBarsBehavior =
                            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                    }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(if (defaultBg) animateProcess.value * 0.8f else 0f))
                .clickable(true) {
                    // 点击背景关闭
                    if (bgClickDismiss) {
                        animateVisible.value = false
                    }
                }
        ) {
            content.invoke(this, animateVisible)
        }

    }

}

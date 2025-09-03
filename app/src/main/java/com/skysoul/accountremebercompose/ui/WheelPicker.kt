package com.skysoul.accountremebercompose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * WheelPicker 滚轮选择器
 *
 * @param items 数据项列表
 * @param onItemSelected 选中项变化回调
 * @param modifier Modifier
 * @param startIndex 初始选中项索引
 * @param textCreator 文本创建器
 * @param textStyle 默认文本样式
 * @param selectedTextStyle 选中文本样式
 * @param itemHeight 每个item的高度
 */
@Composable
fun <T> WheelPicker(
    items: List<T>,
    onItemSelected: (index: Int, item: T) -> Unit,
    modifier: Modifier = Modifier,
    startIndex: Int = 0,
    textCreator: (T) -> String = { it.toString() },
    textStyle: TextStyle = TextStyle(fontSize = 16.sp, color = Color.Gray),
    selectedTextStyle: TextStyle = TextStyle(fontSize = 30.sp, color = Color.Black, fontWeight = FontWeight.Bold),
    itemHeight: Dp = 40.dp
) {
    val itemHeightPx = with(LocalDensity.current) { itemHeight.toPx() }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var viewPortHeight by remember { mutableStateOf(0) }
    var dragging by remember { mutableStateOf(false) }

    LaunchedEffect(startIndex) {
        listState.scrollToItem(startIndex)
    }

    LaunchedEffect(listState.isScrollInProgress, dragging) {
        if (!listState.isScrollInProgress && !dragging) {
            val index = listState.firstVisibleItemIndex
            val offset = listState.firstVisibleItemScrollOffset
            val centerIndex = if (offset > itemHeightPx / 2) {
                index + 1
            } else {
                index
            }
            val validIndex = centerIndex.coerceIn(0, items.size - 1)
            if (validIndex != listState.firstVisibleItemIndex || offset != 0) {
                listState.animateScrollToItem(validIndex)
                onItemSelected(validIndex, items[validIndex])
            } else {
                onItemSelected(validIndex, items[validIndex])
            }
        }
    }

    Box(
        modifier = modifier
            .onSizeChanged { viewPortHeight = it.height }
    ) {
        // 计算顶部和底部的padding，使选中项居中
        val topPadding = with(LocalDensity.current) {
            maxOf(0, (viewPortHeight / 2 - itemHeightPx / 2).toInt()).toDp()
        }
        val bottomPadding = topPadding

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(top = topPadding, bottom = bottomPadding),
            modifier = Modifier
                .fillMaxWidth()
                .draggable(
                    state = rememberDraggableState { delta ->
                        coroutineScope.launch {
                            listState.scrollBy(-delta)
                        }
                    },
                    orientation = Orientation.Vertical,
                    onDragStarted = {
                        dragging = true
                    },
                    onDragStopped = {
                        dragging = false
                    }
                )
        ) {
            items(items.size) { index ->

                val index2 = listState.firstVisibleItemIndex
                val offset = listState.firstVisibleItemScrollOffset
                val centerIndex = if (offset > itemHeightPx / 2) {
                    index2 + 1
                } else {
                    index2
                }
                val validIndex = centerIndex.coerceIn(0, items.size - 1)

                val isSelected = index == validIndex

                // 计算item到中心的距离（以item为单位）
                val distanceToCenter = abs(index - listState.firstVisibleItemIndex)

                // 根据距离计算透明度，越远越透明
                val alpha = 1f - (distanceToCenter * 0.3f).coerceAtMost(1f)

                val offsetRate = offset.toFloat() / itemHeightPx

                var progress = 0f
                if(index == index2){
                    progress = 1-offsetRate
                }else if(index == index2+1){
                    progress = offsetRate
                }

                val textStyleFinal = interpolateTextStyles(
                    textStyle,
                    selectedTextStyle,
                    progress
                )

                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth().clickable {
                            if(index != validIndex){
                                coroutineScope.launch {
                                    listState.animateScrollToItem(index)
                                    onItemSelected(index, items[index])
                                }

                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = textCreator(items[index]),
//                        style = if (isSelected) selectedTextStyle else textStyle,
                        style = textStyleFinal,
                        modifier = Modifier.alpha(alpha)
                    )
                }
            }
        }

        // 选中项的分隔线
        Divider(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = with(LocalDensity.current) { (-itemHeightPx / 2).toDp() })
        )

        Divider(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = with(LocalDensity.current) { (itemHeightPx / 2).toDp() })
        )
    }
}
/**
 * 在两个TextStyle之间进行插值
 *
 * @param style1 起始样式
 * @param style2 目标样式
 * @param progress 插值进度 (0f 到 1f)
 * @return 插值后的样式
 */
fun interpolateTextStyles(style1: TextStyle, style2: TextStyle, progress: Float): TextStyle {
    val fontSize = lerpTextUnit(style1.fontSize, style2.fontSize, progress)
    val color = lerpColor(style1.color, style2.color, progress)
    val fontWeight = lerpFontWeight(style1.fontWeight, style2.fontWeight, progress)

    return style1.merge(style2.copy(
        fontSize = fontSize,
        color = color,
        fontWeight = fontWeight
    ))
}

/**
 * 在两个TextUnit之间进行线性插值
 */
fun lerpTextUnit(unit1: TextUnit, unit2: TextUnit, progress: Float): TextUnit {
    if (!unit1.isSp || !unit2.isSp) return unit1
    return lerp(unit1.value, unit2.value, progress).sp
}

/**
 * 在两个Color之间进行线性插值
 */
fun lerpColor(color1: Color, color2: Color, progress: Float): Color {
    return Color(
        red = lerp(color1.red, color2.red, progress),
        green = lerp(color1.green, color2.green, progress),
        blue = lerp(color1.blue, color2.blue, progress),
        alpha = lerp(color1.alpha, color2.alpha, progress)
    )
}

/**
 * 在两个FontWeight之间进行插值
 */
fun lerpFontWeight(weight1: FontWeight?, weight2: FontWeight?, progress: Float): FontWeight? {
    if (weight1 == null || weight2 == null) return weight1 ?: weight2

    val w1 = weight1.weight.toFloat()
    val w2 = weight2.weight.toFloat()
    val interpolated = lerp(w1, w2, progress)

    return FontWeight(interpolated.toInt())
}

/**
 * 线性插值函数
 */
fun lerp(start: Float, end: Float, progress: Float): Float {
    return start + (end - start) * progress
}
@Composable
fun Divider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.Gray)
    )
}

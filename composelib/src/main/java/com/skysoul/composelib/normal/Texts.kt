package com.skysoul.composelib.normal

import android.provider.CalendarContract.Colors
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp


@Composable
fun toast(text: String?){
    Text("testestt")
}

/**
 * 自定义标签Text
 *
 * @param prefixText    标签前文案，可空
 * @param postfixText   标签后文案，可空
 * @param tagPlaceholderWidth   标签占位宽度
 * @param tagPlaceholderHeight  标签占位高度
 * @param tagAlign      标签对齐方式（垂直方向）
 * @param tag           自定义标签实现
 *
 * 其余参数兼容[Text]，描述标签前后文案的属性
 */
@Composable
fun TagText(
    modifier: Modifier = Modifier,
    prefixText: String? = null,
    postfixText: String? = null,
    tagPlaceholderWidth: TextUnit = 1.sp,
    tagPlaceholderHeight: TextUnit = 1.sp,
    tagAlign: PlaceholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
    tag: @Composable () -> Unit
) {
    val inlineContent = mapOf(
        "tag" to InlineTextContent(
            placeholder = Placeholder(tagPlaceholderWidth, tagPlaceholderHeight, tagAlign)
        ) {
            tag()
        }
    )

    Text(
        text = buildAnnotatedString {
            append(prefixText ?: "")
            appendInlineContent("tag")
            append(postfixText ?: "")
        },
        inlineContent = inlineContent,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        style = style
    )
}

/**
 * 背景半圆矩形的Button
 *
 * @param modifier 控件尺寸，布局外部间距，点击等由使用者传入
 * @param backgroundColor 背景颜色，在[backgroundBrush]为空时才被使用
 * @param backgroundBrush 背景渐变效果，优先于[backgroundColor]使用
 * @param backgroundShape 默认半圆形圆角
 *
 * 其余参数兼容[Text]，描述Button的文案部分
 */
@Composable
fun TextButton(
    modifier: Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    backgroundBrush: Brush? = null,
    backgroundShape: Shape = RoundedCornerShape(50),
    text: String = "",
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    val newModifier = if (backgroundBrush != null) {
        modifier.background(brush = backgroundBrush, shape = backgroundShape)
    } else {
        modifier.background(color = backgroundColor, shape = backgroundShape)
    }
    Box(
        modifier = newModifier,
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            color = color,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = style
        )
    }
}
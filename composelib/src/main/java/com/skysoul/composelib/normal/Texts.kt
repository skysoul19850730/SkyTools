package com.skysoul.composelib.normal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.AnnotatedString.Builder
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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp



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


@Composable
fun CustomText(
    text: String,
    modifier: Modifier = Modifier,
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
    onTextLayout: ((TextLayoutResult) -> Unit) = {},
    style: TextStyle = LocalTextStyle.current,
    rangeBuilder: MutableList<TextRangeReplace>.() -> Unit
) {

    val rangeList = arrayListOf<TextRangeReplace>()
    rangeBuilder(rangeList)


    val inlineContent = hashMapOf<String, InlineTextContent>()

    val annotationStrBuilder = Builder()

    if (rangeList.isEmpty()) {
        annotationStrBuilder.append(text)
    } else {
        rangeList.forEachIndexed { index, item ->

            //pre
            if (index == 0) {
                annotationStrBuilder.append(text.substring(0, item.start))
            }

            //item
            if (item.inLineContent != null) {
                inlineContent[item.key] = item.inLineContent
                annotationStrBuilder.appendInlineContent(item.key)
            } else if (item.replaceAnnotation != null) {
                annotationStrBuilder.pushStringAnnotation(item.key, item.replaceAnnotation.showText)
                annotationStrBuilder.withStyle(item.replaceAnnotation.style) {
                    append(item.replaceAnnotation.showText)
                }
                annotationStrBuilder.pop()
            } else {
                annotationStrBuilder.append(text.substring(item.start, item.end))
            }

            //end
            if (index == rangeList.size - 1) {
                annotationStrBuilder.append(text.substring(item.end))
            } else {
                annotationStrBuilder.append(text.substring(item.end, rangeList[index + 1].start))
            }

        }
    }
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    val annotationString = annotationStrBuilder.toAnnotatedString()



    Text(
        annotationString,
        modifier.drawWithContent {
            drawContent()
            val layoutResult = layoutResult.value ?: return@drawWithContent
            val ranges = annotationString.getStringAnnotations(0, annotationString.length)
            rangeList.filter { it.replaceAnnotation != null }.forEach {

                val range = ranges.firstOrNull{range->
                    range.tag == it.key
                }?:return@forEach

                val list: ArrayList<List<Rect>> = arrayListOf()

                var lineList = arrayListOf<Rect>()
                var lastLine = -1
                for (i in range.start until range.end) {
                    val rect = layoutResult.getBoundingBox(i)
                    val curLine = layoutResult.getLineForOffset(i)
                    if (curLine == lastLine) {
                        lineList.add(rect)
                    } else {
                        //不同行
                        if(lineList.size>0) {
                            list.add(lineList)
                        }
                        lineList = arrayListOf(rect)
                        lastLine = curLine
                    }
                }
                if(lineList.size>0) {
                    list.add(lineList)
                }
                it.replaceAnnotation!!.drawCanvas.invoke(this,list)

            }

        },
        color,
        autoSize = null,
        fontSize,
        fontStyle,
        fontWeight,
        fontFamily,
        letterSpacing,
        textDecoration,
        textAlign,
        lineHeight,
        overflow,
        softWrap,
        maxLines,
        minLines,
        inlineContent = inlineContent,
        onTextLayout = {
            layoutResult.value = it
            onTextLayout(it)
        },
        style
    )
}
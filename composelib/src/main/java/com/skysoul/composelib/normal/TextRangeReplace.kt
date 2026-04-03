package com.skysoul.composelib.normal

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.SpanStyle

/**
 *@author shenqichao
 *Created on 2026/1/8
 *@Description
 * end  exclude
 */
data class TextRangeReplace(
    val start: Int,
    val end: Int,
    val inLineContent: InlineTextContent?,
    val replaceAnnotation: TextRangeReplaceAnnotation?
){
    val key: String
        get() = "$start-$end"
}

//每一个的每个字的rect，外面既可以按字处理，也可以按行处理
data class TextRangeReplaceAnnotation(
    val style: SpanStyle,
    val showText: String,
    val drawCanvas: DrawScope.(List<List<Rect>>) -> Unit
)

fun MutableList<TextRangeReplace>.addRangeReplace(rangeData:TextRangeReplace){
    add(rangeData)
}
fun MutableList<TextRangeReplace>.addRangeReplace(start:Int,end: Int,content: InlineTextContent){
    addRangeReplace(TextRangeReplace(start,end,content,null))
}
fun MutableList<TextRangeReplace>.addRangeReplace(start:Int,end: Int,content: TextRangeReplaceAnnotation){
    addRangeReplace(TextRangeReplace(start,end,null,content))
}
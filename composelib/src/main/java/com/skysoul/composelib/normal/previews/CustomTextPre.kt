package com.skysoul.composelib.normal.previews

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skysoul.composelib.R
import com.skysoul.composelib.normal.CustomText
import com.skysoul.composelib.normal.TextRangeReplaceAnnotation
import com.skysoul.composelib.normal.addRangeReplace
import com.skysoul.composelib.normal.continuousRotate360
import com.skysoul.composelib.normal.rotate360

/**
 *@author shenqichao
 *Created on 2026/1/9
 *@Description
 */
@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun CustomTextPre() {
    var rotate = remember  { mutableStateOf(true) }
    CustomText(
        text = "白日依山尽，黄河入海流。是发发发沙发沙发发生发发发顺丰",
        Modifier
            .width(200.dp)
            .background(Color.White)
            .padding(12.dp),
        Color.Black,
        rangeBuilder = {
            addRangeReplace(
                0, 0, InlineTextContent(
                    placeholder = Placeholder(
                        width = 20.sp,
                        height = 20.sp,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                    )
                ) {
                    Box(Modifier.fillMaxSize().continuousRotate360(rotate.value), contentAlignment = Alignment.Center){
                        Text("HH", color = Color.Blue)
                    }
                })
            addRangeReplace(
                3, 5, TextRangeReplaceAnnotation(
                SpanStyle(color = Color.Red),
                "什么情况",
                { list ->
                    list.forEach {

                        drawLine(Color.Red, it.first().bottomLeft, it.last().bottomRight, 3f)

                    }
                }
            ))
            addRangeReplace(
                8, 10, TextRangeReplaceAnnotation(
                SpanStyle(color = Color.Red),
                "向哪里",
                { list ->
                    list.forEach {
                        drawLine(Color.Red, it.first().bottomLeft, it.last().bottomRight, 3f)

                    }
                }
            ))
        }
    )
}
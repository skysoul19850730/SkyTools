package com.skysoul.accountremebercompose.activities.main

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextRange.Companion
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skysoul.accountremebercompose.R
import com.skysoul.accountremebercompose.managers.UserManager
import com.skysoul.accountremebercompose.ui.HSpace16
import com.skysoul.accountremebercompose.ui.VSpace
import com.skysoul.accountremebercompose.ui.bar.topBarPage
import com.skysoul.accountremebercompose.utils.SharedPreferencesManager

/**
 *@author shenqichao
 *Created on 2025/4/1
 *@Description
 */
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class,
    ExperimentalLayoutApi::class
)
@Composable
fun SearchPage(
    mViewModel: MainViewModel,
    sharedTransitionScope: SharedTransitionScope,
    showSearch: MutableState<Boolean>,
    clickListener: (ClickView, any: Any?) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberLazyListState()

    var textValue = remember { mutableStateOf( TextFieldValue("")) }
    var doSearchText by remember { mutableStateOf("") }


    val history = remember { mutableStateListOf<String>() }

    LaunchedEffect("") {
        val his = SharedPreferencesManager.getString("history_${UserManager.getUserId()}")
        if (!his.isNullOrEmpty()) {
            history.addAll(his.split(","))
        }
        focusRequester.requestFocus()
//        keyboardController?.show()
    }
//    DisposableEffect("") {
//
//        onDispose {
//            keyboardController?.hide()
//        }
//    }

    with(sharedTransitionScope) {

        topBarPage(topbar = {

            Row(
                Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .sharedElementWithCallerManagedVisibility(
                        rememberSharedContentState("search"),
                        showSearch.value
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HSpace16()
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "", Modifier.clickable {
                    showSearch.value = false
                })
                HSpace16()
                BasicTextField(textValue.value,
                    onValueChange = {
                        textValue.value = it.copy(it.text.take(10))
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .focusRequester(focusRequester)
                        .background(Color(0xfff5f6f7), RoundedCornerShape(4.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.merge(
                        color = Color(0xffa7aaaf),
                        fontSize = 15.sp
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions {
                        doSearchText = textValue.value.text
                        if (doSearchText.isNotEmpty()) {
                            if (!history.contains(doSearchText)) {
                                history.add(doSearchText)
                                if (history.size > 10) {
                                    history.removeRange(0, history.size - 10)
                                }
                                SharedPreferencesManager.setString(
                                    "history_${UserManager.getUserId()}",
                                    history.joinToString(",")
                                )
                            }
                        }
                    },
                    cursorBrush = Brush.radialGradient(
                        listOf(Color(0xff38d39b), Color(0xff38d39b)),
                        radius = 2.dp.value
                    ),
                    decorationBox = { innerTextField ->

                        Row(
                            Modifier
                                .fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(Modifier.weight(1f)) {
                                innerTextField()
                            }


                            if (textValue.value.text.isNotEmpty()) {
                                Image(
                                    painterResource(R.mipmap.ic_close_bg_grey), "",
                                    Modifier
                                        .size(22.dp)
                                        .clickable {
                                            textValue.value = TextFieldValue("")
                                            doSearchText = ""
                                            keyboardController?.show()
                                        })
                            }

                        }


                    })
                HSpace16()

            }
        }) {

            Box(modifier = Modifier.pointerInput(""){
                detectTapGestures(onPress = {
                    keyboardController?.hide()
                })
            } .padding(16.dp)) {
                if (doSearchText.isEmpty()) {

                    if (history.isNotEmpty()) {
                        Column {
                            Text("搜索历史：")
                            VSpace(dp = 12)
                            FlowRow {

                                history.forEach {

                                    Box(Modifier.padding(start = 0.dp, top = 4.dp, bottom = 4.dp, end = 12.dp)
                                        .background(Color(0xfff7f7f7), RoundedCornerShape(4.dp))
                                        .clickable {
                                            textValue.value = TextFieldValue(it, selection = TextRange(it.length))
                                            doSearchText = it
                                            keyboardController?.show()
                                        }
                                        .padding(horizontal = 10.dp)){
                                        Text(it)
                                    }


                                }
                            }
                        }
                    }

                } else {

                    val accounts =
                        mViewModel.searchAccounts(doSearchText).collectAsState(arrayListOf())



                    LazyColumn(modifier = Modifier.fillMaxSize(), state = scrollState) {

                        itemsIndexed(accounts.value) { index, item ->

                            if (index == 0) {
                                VSpace(dp = 12)
                            }
                            AccountItem(item)
                            VSpace(dp = 12)

                        }


                    }

                    if (accounts.value.isNullOrEmpty()) {
                        Text("没有搜索到结果")
                    }

                }
            }

        }


    }

}
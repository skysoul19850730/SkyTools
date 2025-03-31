package com.skysoul.accountremebercompose.ui.dialog

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.skysoul.accountremebercompose.ui.VSpace16
import com.skysoul.accountremebercompose.utils.log

/**
 *@author shenqichao
 *Created on 2022/9/9
 *@Description
 */
public interface ListDialogItem {
    fun getTitle(): String
}

@Composable
fun <T : ListDialogItem> listCheckDialog(
    showState: MutableState<Boolean>,
    tList: () -> List<T>,
    onChecked: (T) -> Unit
) {
    if (showState.value) {
        Dialog(
            onDismissRequest = { showState.value = false },
        ) {
            Card() {
                Column(
                    Modifier
                        .height(400.dp)
                        .width(300.dp)
                )
                {
                    Text(text = "请选择：", Modifier.padding(16.dp))
                    LazyColumn {
                        items(tList.invoke()) {
                            Text(text = it.getTitle(),
                                modifier = Modifier
                                    .fillMaxWidth()

                                    .clickable {
                                        onChecked.invoke(it)
                                        showState.value = false
                                    }
                                    .padding(horizontal = 16.dp, vertical = 16.dp)

                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NormalDialog(
    showState: MutableState<Boolean>,
    title: String,
    callbackOk: () -> Unit,
    callbackDismiss: () -> Unit = { showState.value = false },
    okText: String = "确定",
    cancelText: String = "取消",
    content: @Composable (() -> Unit)? = null,
) {
    if (showState.value) {

        AlertDialog(onDismissRequest = {
            log("dialog is dismiss")
            showState.value = false
            callbackDismiss.invoke()
        }, confirmButton = {
            TextButton(onClick = { callbackOk.invoke() }) {
                Text(text = okText)
            }
        }, dismissButton = {
            TextButton(onClick = { callbackDismiss.invoke() }) {
                Text(text = cancelText)
            }
        }, title = {
            Text(text = title)
        }, text = content)

    }
}

@Composable
fun inputDialogOne(showState: MutableState<Boolean>, callback: (String) -> Unit) {
    var result = remember {
        mutableStateOf("")
    }
    if (!showState.value) {
        result.value = ""
    }
    NormalDialog(
        showState = showState,
        title = "请输入:",
        callbackOk = { callback.invoke(result.value) }) {
        Card() {
            Column(Modifier.padding(12.dp)) {
                VSpace16()
                OutlinedTextField(value = result.value, onValueChange = {
                    result.value = it
                })
                VSpace16()
            }
        }
    }
}

@Composable
fun TextDialog(
    showState: MutableState<Boolean>,
    title: String,
    content: String,
    callbackOk: () -> Unit,
    callbackDismiss: () -> Unit = { showState.value = false },
    okText: String = "确定",
    cancelText: String = "取消"
) {
    NormalDialog(
        showState = showState,
        title = title,
        callbackOk = callbackOk,
        callbackDismiss = callbackDismiss,
        okText = okText,
        cancelText = cancelText
    ) {
        Text(text = content)
    }
}
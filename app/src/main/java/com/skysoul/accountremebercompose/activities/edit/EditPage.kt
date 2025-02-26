package com.skysoul.accountremebercompose.activities.edit

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skysoul.accountremebercompose.launch
import com.skysoul.accountremebercompose.ui.HSpace16
import com.skysoul.accountremebercompose.ui.VSpace
import com.skysoul.accountremebercompose.ui.VSpace16
import com.skysoul.accountremebercompose.ui.bar.Action
import com.skysoul.accountremebercompose.ui.bar.topBar
import com.skysoul.accountremebercompose.ui.bar.topBarPage
import com.skysoul.accountremebercompose.ui.dialog.inputDialogOne
import com.skysoul.accountremebercompose.ui.dialog.listCheckDialog
import com.skysoul.accountremebercompose.ui.theme.Typography
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *@author shenqichao
 *Created on 2022/9/9
 *@Description
 */

class EditInputBean(
    val title: String,
    val need: Boolean = true,
    val hint: String
) {
    var input: String by mutableStateOf("")
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun editPage(mViewModel: EditViewModel = viewModel()) {

    var showCateDialog: MutableState<Boolean> = remember { mutableStateOf(false) }
    var showCateAdd = remember { mutableStateOf(false) }

    var scope = rememberCoroutineScope()

    topBarPage(
        topbar = {
            topBar(
                showBack = true,
                title = "编辑",
                onBackPressed = { mViewModel.finish.postValue(true) },
                actions = arrayOf(Action.TextAction("保存") { mViewModel.save() })
            )
        }
    ) {
        Log.d("xxxxxx", "paddingvalues $it")

        val state = rememberScrollState()

        Column(
            Modifier
                .imePadding()
                .verticalScroll(state)

        ) {

            mViewModel.editBeans.forEachIndexed { index, editInputBean ->
                if (index == 0) {
                    VSpace(dp = 16)
                }
                editItem(bean = editInputBean)
                VSpace(dp = 16)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Text(text = "所属分类：")
                Button(onClick = { showCateDialog.value = true }, Modifier.weight(1f)) {
                    Text(text = mViewModel.cateSelected?.cateName ?: "")
                    Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
                }

                HSpace16()
                Text(text = "添加", Modifier.clickable {
                    showCateAdd.value = true
                })
                HSpace16()
            }

            VSpace16()
            Text(text = "额外属性", modifier = Modifier.padding(horizontal = 12.dp))
            VSpace16()
            if (mViewModel.externalColumn.size > 0) {
                Column(Modifier.padding(horizontal = 12.dp)) {
                    mViewModel.externalColumn.forEachIndexed { index, c ->
                        var key: String by mutableStateOf(c.key)
                        var value: String by mutableStateOf(c.value)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            HSpace16()
                            OutlinedTextField(
                                value = key, onValueChange = {
                                    key = it
                                    c.key = it
                                },
                                Modifier
                                    .weight(1f)
                            )
                            Text(text = ":", Modifier.padding(horizontal = 12.dp))
                            OutlinedTextField(
                                value = value,
                                onValueChange = {
                                    value = it
                                    c.value = it
                                },
                                Modifier
                                    .weight(1f)
                            )
                            HSpace16()
                            Text(text = "删除", Modifier.clickable {
                                mViewModel.removeColumn(index)
                            })
                            HSpace16()
                        }
                        VSpace(8)
                    }
                }
            }
            Box(
                modifier = Modifier
                    .border(1.dp, Color.LightGray)
                    .width(200.dp)
                    .height(40.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        mViewModel.addEmptyColumn()
                        scope.launch {
                            delay(300)
                            state.animateScrollTo(state.maxValue)
                        }

                    }) {
                Text(text = "添加", Modifier.align(Alignment.Center))
            }
            VSpace16()

        }
        listCheckDialog(
            showState = showCateDialog,
            tList = { mViewModel.cateList },
            onChecked = {
                mViewModel.cateSelected = it
            })
        inputDialogOne(showState = showCateAdd, callback = {
            if (it.isNotEmpty()) {
                mViewModel.launch {
                    val result = mViewModel.addCateAndChoose(it)
                    if (result) {

                        showCateAdd.value = false
                    } else {
                        mViewModel.showToast("添加失败，请更换名称")
                    }
                }

            }
        })
    }
}

@Composable
fun editItem(bean: EditInputBean) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        var title = bean.title
        if (!bean.need) {
            title += "(选填)"
        }
        Text(text = "${title}:")
        HSpace16()
        OutlinedTextField(modifier = Modifier
            .weight(1f), value = bean.input, onValueChange = { bean.input = it },
            placeholder = { Text(text = bean.hint) }
        )
//        TextField(
//            modifier = Modifier
//                .weight(1f),
//            value = bean.input,
//            onValueChange = { bean.input = it },
//            placeholder = { Text(text = bean.hint) })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors()
) {
    // If color is not provided via the text style, use content color as a default
    val textColor = textStyle.color
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    CompositionLocalProvider(
        LocalTextSelectionColors provides TextSelectionColors(
            handleColor = Color(0xFF4286F4),
            backgroundColor = Color(0xFF4286F4).copy(alpha = 0.4f)
        )
    ) {
        BasicTextField(
            value = value,
            modifier = if (label != null) {
                modifier
                    // Merge semantics at the beginning of the modifier chain to ensure padding is
                    // considered part of the text field.
                    .semantics(mergeDescendants = true) {}
                    .padding(top = 12.dp)
            } else {
                modifier
            }
                .defaultMinSize(
                    minWidth = OutlinedTextFieldDefaults.MinWidth,
                    minHeight = OutlinedTextFieldDefaults.MinHeight
                ),
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = mergedTextStyle,
            cursorBrush = SolidColor(Color.Red),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            decorationBox = @Composable { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = value,
                    visualTransformation = visualTransformation,
                    innerTextField = innerTextField,
                    placeholder = placeholder,
                    label = label,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    prefix = prefix,
                    suffix = suffix,
                    supportingText = supportingText,
                    singleLine = singleLine,
                    enabled = enabled,
                    isError = isError,
                    interactionSource = interactionSource,
                    colors = colors,
                    contentPadding = PaddingValues(6.dp),
                    container = {
                        OutlinedTextFieldDefaults.ContainerBox(
                            enabled,
                            isError,
                            interactionSource,
                            colors,
                            shape
                        )
                    }
                )
            }
        )
    }
}

@Preview
@Composable
fun test() {
    editItem(bean = EditInputBean("测试", false, "填写测试内容"))
}
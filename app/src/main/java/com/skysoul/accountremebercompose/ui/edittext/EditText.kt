package com.skysoul.accountremebercompose.ui.edittext

import android.widget.EditText
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
 *@author shenqichao
 *Created on 2022/8/29
 *@Description
 */

@Composable
fun EditText(
    state: TextFieldState,
    label: String,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    onChange:((String)->Unit)?=null
) {
    OutlinedTextField(value = state.text, onValueChange = {
        state.text = it
        state.errorText = ""
        onChange?.invoke(it)
    },
        label = {
            Text(text = label, style = style)
        },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                state.onFocusChange(focusState.isFocused)
            },
        textStyle = style,
        isError = !state.errorText.isNullOrEmpty()
    )
    if(!state.errorText.isNullOrEmpty()){
        TextFieldError(textError = state.errorText)
    }
}

@Composable
fun PasswordEditText(
    state: TextFieldState,
    label: String,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
) {
    val showPassword = remember { mutableStateOf(false) }
    OutlinedTextField(value = state.text, onValueChange = {
        state.text = it
        state.errorText = ""
    },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        label = {
            Text(text = label, style = style)
        },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                state.onFocusChange(focusState.isFocused)
            },
        textStyle = style,
        isError = !state.errorText.isNullOrEmpty(),
        trailingIcon = {
            if (showPassword.value) {
                IconButton(onClick = { showPassword.value = false }) {
                    Icon(
                        imageVector = Icons.Filled.Visibility,
                        ""
                    )
                }
            } else {
                IconButton(onClick = { showPassword.value = true }) {
                    Icon(
                        imageVector = Icons.Filled.VisibilityOff,
                        ""
                    )
                }
            }
        },
        visualTransformation = if (showPassword.value) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
    )
    if(!state.errorText.isNullOrEmpty()){
        TextFieldError(textError = state.errorText)
    }
}

@Composable
fun TextFieldError(textError: String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)) {
        Text(
            text = textError,
            modifier = Modifier.fillMaxWidth(),
            style = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.error)
        )
    }
}
package com.skysoul.accountremebercompose.ui.edittext

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

open class TextFieldState() {
    var text: String by mutableStateOf("")
    var errorText: String by mutableStateOf("")

    // was the TextField ever focused
    var isFocused: Boolean by mutableStateOf(false)

    val enable: Boolean
        get() = when {
            text.isEmpty() -> false
            errorText.isNotEmpty() -> false
            else -> true
        }

    fun onFocusChange(focused: Boolean) {
        isFocused = focused
    }

    fun checkNull(errorMsg: String): Boolean? {
        if (text.isEmpty()) {
            errorText = errorMsg
            return null
        }
        return false
    }
}

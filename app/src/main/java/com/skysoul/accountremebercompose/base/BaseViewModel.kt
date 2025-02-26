package com.skysoul.accountremebercompose.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 *@author shenqichao
 *Created on 2022/8/29
 *@Description
 */
open class BaseViewModel : ViewModel() {
    var isLoading: Boolean by mutableStateOf(false)
    val finish = MutableLiveData(false)
    val next = MutableLiveData(false)
    var toast = MutableLiveData("")

    fun showLoading() {
        isLoading = true
    }

    fun hideLoading() {
        isLoading = false
    }

    fun showToast(s: String) {
        toast.postValue(s)
    }


}
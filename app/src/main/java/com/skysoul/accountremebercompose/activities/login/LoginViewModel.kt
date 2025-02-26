package com.skysoul.accountremebercompose.activities.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import com.skysoul.accountremebercompose.base.BaseViewModel
import com.skysoul.accountremebercompose.launch
import com.skysoul.accountremebercompose.managers.UserManager
import com.skysoul.accountremebercompose.model.beans.User
import com.skysoul.accountremebercompose.model.repository.local.UserRepositoryLocal
import com.skysoul.accountremebercompose.ui.edittext.TextFieldState
import com.skysoul.accountremebercompose.utils.SharedPreferencesManager
import com.skysoul.accountremebercompose.utils.log

/**
 *@author shenqichao
 *Created on 2022/8/29
 *@Description
 */
class LoginViewModel : BaseViewModel() {

    val userNameState = TextFieldState()
    val passwordState = TextFieldState()

//    var lastUser: MutableLiveData<User?> = MutableLiveData()

    val userRepository: UserRepositoryLocal = UserRepositoryLocal()

    val registerType = MutableLiveData(-1)

    val passTip = mutableStateOf("")

    fun getLastUser() {
        launch {
            showLoading()
            SharedPreferencesManager.getString(SharedPreferencesManager.lastUserName)?.run {
                userNameState.text = this
            }
            val user = UserManager.getLastUser()
            if (user != null) {
                jumpWithUser(user)
            }
            hideLoading()
        }
    }

    fun getTips() {
        launch {
            userRepository.getUserTipByUsername(userNameState.text).ifSuccess {
                passTip.value = it
            }.ifError {
                passTip.value = "无法获取提示信息"
            }
        }
    }

    fun jumpWithUser(user: User?) {
        if (user != null) {
            if (user.nickName.isNullOrEmpty()) {
                registerType.postValue(2)
            } else {
                registerType.postValue(0)
            }
        } else {
            registerType.postValue(1)
        }
    }

    fun login() {
        launch {
            val username = userNameState.text
            if (username.isBlank()) {
                userNameState.errorText = "请填写用户名"
                return@launch
            }
            val password = passwordState.text
            if (password.isBlank()) {
                passwordState.errorText = "请填写密码"
                return@launch
            }
            showLoading()
            log("showloading")
            userRepository.checkPasswordWithType(0, username, password).ifSuccess {
                it.run {
                    when (errorType) {
                        0 -> {
                            UserManager.save4LastUser(username, user!!.userId)
                            jumpWithUser(user)
                        }
                        -1 -> {
                            userNameState.errorText = "用户不存在"
                        }
                        -3 -> {
                            passwordState.errorText = "密码错误，还可以尝试${leftTimes}次"
                        }
                        -2 -> {
                            passwordState.errorText = "密码错误，请再${nextTryTime}小时后尝试"
                        }
                    }
                }
            }.ifError {
                log("${it.message}")
            }
            log("hideloading")
            hideLoading()
        }
    }
}
package com.skysoul.accountremebercompose.activities.login

import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import com.skysoul.accountremebercompose.base.BaseViewModel
import com.skysoul.accountremebercompose.launch
import com.skysoul.accountremebercompose.managers.UserManager
import com.skysoul.accountremebercompose.model.SSResult
import com.skysoul.accountremebercompose.model.beans.User
import com.skysoul.accountremebercompose.model.repository.local.UserRepositoryLocal
import com.skysoul.accountremebercompose.ui.edittext.TextFieldState
import com.skysoul.accountremebercompose.utils.SharedPreferencesManager
import com.skysoul.accountremebercompose.utils.log
import kotlinx.coroutines.GlobalScope

/**
 *@author shenqichao
 *Created on 2022/8/29
 *@Description
 */
class LoginViewModel : BaseViewModel() {

    val userNameState = TextFieldState()
    val passwordState = TextFieldState()

//    var lastUser: MutableLiveData<User?> = MutableLiveData()

    private val userRepository: UserRepositoryLocal = UserRepositoryLocal()

    val registerType = MutableLiveData(-1)

    val passTip = mutableStateOf("")

    private suspend fun getTheLastUser(): User? {
        val curUid = SharedPreferencesManager.getInt(SharedPreferencesManager.userid, 0)
        return if (curUid == 0) null else userRepository.getUserById(curUid)?.run {
            when (this) {
                is SSResult.Success -> {
                    data
                }
                else -> null
            }
        }
    }

    fun getLastUser() {
        launch {
            showLoading()
            SharedPreferencesManager.getString(SharedPreferencesManager.lastUserName)?.run {
                userNameState.text = this
            }
            var user = getTheLastUser()
            if (user != null) {
                if(user!!.lastLoginTime!=0L && System.currentTimeMillis() - user!!.lastLoginTime>14*24*60*60*1000){
                    user = null
                }
            }
            jumpWithUser(user)
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
                launch {
                    UserManager.logSuc(user)
                    registerType.postValue(0)
                }
            }
        }
    }

    fun toRegisterAct(){
        registerType.postValue(1)
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
                            if(user!=null) {
                                userRepository.getUserById(user!!.userId).ifSuccess {
                                    jumpWithUser(it)
                                }.ifError {
                                    log(it.message)
                                }

                            }

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
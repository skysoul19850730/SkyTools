package com.skysoul.accountremebercompose.activities.register2

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import com.skysoul.accountremebercompose.base.BaseViewModel
import com.skysoul.accountremebercompose.launch
import com.skysoul.accountremebercompose.managers.UserManager
import com.skysoul.accountremebercompose.model.api.NilException
import com.skysoul.accountremebercompose.model.beans.User
import com.skysoul.accountremebercompose.model.repository.local.UserRepositoryLocal
import com.skysoul.accountremebercompose.ui.edittext.TextFieldState
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 *@author shenqichao
 *Created on 2022/8/31
 *@Description
 */
class RegisterViewModel2 : BaseViewModel() {

    var userNameState = TextFieldState()
    var passwordState = TextFieldState()

    var usePassLogin: Boolean by mutableStateOf(false)

    val userRepository: UserRepositoryLocal = UserRepositoryLocal()

    val dialogNoPass: MutableLiveData<(suspend (Boolean) -> Unit)?> = MutableLiveData(null)

    var di2: ((Boolean) -> Unit)? by mutableStateOf(null)

    private suspend fun showSureDialog() = suspendCancellableCoroutine<Boolean> {
//        dialogNoPass.value = { result ->
//            it.resume(result)
//        }
        di2 = { result ->
            di2 = null
            it.resume(result)
        }
    }

    fun doOver() {
        userNameState.checkNull("昵称不能为空") ?: return
        var userLogined = UserManager.getUser() ?: return
        launch {

            userRepository.isNickNameExist(userNameState.text).ifSuccess {
                if (it) {
                    userNameState.errorText = "昵称已存在，请更换"
                    return@launch
                }
            }.ifError {
                if(it !is NilException){
                    return@launch
                }
            }

            val goon =
                if (!usePassLogin && passwordState.text.isNullOrEmpty()) {
                    showSureDialog()
                } else {
                    true
                }
            if (!goon) {
                return@launch
            }


            var user = User()
            user.userId = userLogined.userId
            user.nickName = userNameState.text

            userRepository.updateUserBaseInfo(user)
            userLogined.nickName = user.nickName
            userRepository.updateUserPassword4ViewAccount(
                userLogined.userId,
                passwordState.text,
                usePassLogin
            ).ifSuccess {
                showToast("信息完成")
                next.postValue(true)
            }.ifError {
                showToast("信息填写有误")
            }

        }
    }
}
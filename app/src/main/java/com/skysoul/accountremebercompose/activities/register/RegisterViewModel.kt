package com.skysoul.accountremebercompose.activities.register

import com.skysoul.accountremebercompose.base.BaseViewModel
import com.skysoul.accountremebercompose.launch
import com.skysoul.accountremebercompose.managers.UserManager
import com.skysoul.accountremebercompose.model.beans.Cate
import com.skysoul.accountremebercompose.model.repository.local.CateResLocal
import com.skysoul.accountremebercompose.model.repository.local.UserRepositoryLocal
import com.skysoul.accountremebercompose.ui.edittext.TextFieldState

/**
 *@author shenqichao
 *Created on 2022/8/31
 *@Description
 */
class RegisterViewModel : BaseViewModel() {

    var userNameState = TextFieldState()
    var passwordState = TextFieldState()
    var passwordAgainState = TextFieldState()
    var passwordTipState = TextFieldState()

    val userRepository: UserRepositoryLocal = UserRepositoryLocal()
    val cateRepository: CateResLocal = CateResLocal()

    fun register() {
        userNameState.checkNull("用户名不能为空") ?: return
        passwordState.checkNull("密码不能为空") ?: return
        passwordAgainState.checkNull("密码确认不能为空") ?: return

        if (passwordState.text != passwordAgainState.text) {
            passwordAgainState.errorText = "两次密码不一致"
            return
        }

        launch {
            userRepository.isUserNameExist(userNameState.text).ifSuccess {
                if (it) {
                    userNameState.errorText = "用户名已存在，请更换"
                    return@launch
                }
            }.ifError {
                return@launch
            }

            userRepository.registerWith(
                userNameState.text,
                passwordState.text,
                passwordTipState.text
            ).ifSuccess {
                showToast("注册成功")
                UserManager.save4LastUser(it.userName,it.userId)
                cateRepository.addCate(Cate(0,"默认分类",it.userId))
                next.postValue(true)
            }.ifError {
                showToast("注册失败")
            }

        }
    }
}
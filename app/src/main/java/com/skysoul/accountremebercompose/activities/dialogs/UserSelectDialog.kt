package com.skysoul.accountremebercompose.activities.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import com.skysoul.accountremebercompose.model.repository.local.UserRepositoryLocal

/**
 *@author shenqichao
 *Created on 2026/4/3
 *@Description
 */
@Composable
fun UserSelectDialog(visible: MutableState<Boolean>) {

    CustomDialog(visible) {


        LaunchedEffect(Unit) {
            val userRepository: UserRepositoryLocal = UserRepositoryLocal()
            val users = userRepository.getNoLoginUserList()
        }

    }

}
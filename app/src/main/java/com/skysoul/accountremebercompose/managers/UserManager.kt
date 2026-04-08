package com.skysoul.accountremebercompose.managers

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import com.skysoul.accountremebercompose.activities.login.LoginActivity
import com.skysoul.accountremebercompose.data.dbroom.AccountDatabase
import com.skysoul.accountremebercompose.data.dbroom.daos.UserDao
import com.skysoul.accountremebercompose.data.dbroom.entities.DMMember
import com.skysoul.accountremebercompose.model.beans.User
import com.skysoul.accountremebercompose.model.repository.local.UserRepositoryLocal
import com.skysoul.accountremebercompose.utils.SharedPreferencesManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 *@author shenqichao
 *Created on 2022/8/29
 *@Description
 */
object UserManager {

    val userState = mutableStateOf<User?>(null)

//    val userDao: UserRepositoryLocal = UserRepositoryLocal()

    val memberState = mutableStateOf<DMMember?>(null)

    val currentMemberId: Int
        get() = userState.value?.currentMember?.id?:0

    val userId: Int
        get() = userState.value?.userId?:0

    fun logout(activity: Activity?=null) {
        SharedPreferencesManager.setInt(SharedPreferencesManager.userid, 0)
        userState.value = null
        activity?.apply {
            finish()
            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                ))
        }
    }

    fun changeMember(member: DMMember){
        memberState.value = member
        GlobalScope.launch {
            UserRepositoryLocal().updateUserBaseInfo(userState.value!!.apply {
                currentMember = member
            })
        }

    }

    fun addMember(member: DMMember){
        userState.value?.members?.add(member)
    }

    suspend fun logSuc(user: User) {
        SharedPreferencesManager.setString(SharedPreferencesManager.lastUserName, user.userName)
        SharedPreferencesManager.setInt(SharedPreferencesManager.userid, user.userId)
        user.lastLoginTime = System.currentTimeMillis()
        UserRepositoryLocal().updateUserBaseInfo(user)
        userState.value = user
        memberState.value = user.currentMember
    }
}
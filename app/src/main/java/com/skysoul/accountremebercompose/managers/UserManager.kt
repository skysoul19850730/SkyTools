package com.skysoul.accountremebercompose.managers

import androidx.lifecycle.MutableLiveData
import com.skysoul.accountremebercompose.model.SSResult
import com.skysoul.accountremebercompose.model.beans.User
import com.skysoul.accountremebercompose.model.repository.local.UserRepositoryLocal
import com.skysoul.accountremebercompose.utils.SharedPreferencesManager

/**
 *@author shenqichao
 *Created on 2022/8/29
 *@Description
 */
object UserManager {

    val userLiveData :MutableLiveData<User> = MutableLiveData(null)

    private var mUser: User? = null

    val userDao: UserRepositoryLocal = UserRepositoryLocal()

    fun getUser(): User? {
        return mUser
    }

    fun getUserId(): Int {
        return getUser()?.userId ?: 0
    }

    fun logout(){
        SharedPreferencesManager.setInt(SharedPreferencesManager.userid,0)
        userLiveData.postValue(null)
    }

    suspend fun getLastUser(): User? {
        val curUid = SharedPreferencesManager.getInt(SharedPreferencesManager.userid, 0)
        return if (curUid == 0) null else userDao.getUserById(curUid)?.run {
            when (this) {
                is SSResult.Success -> {
                    mUser = data
                    userLiveData.postValue(mUser)
                    data
                }
                else -> null
            }
        }
    }

    suspend fun save4LastUser(userName: String, userId: Int) {
        SharedPreferencesManager.setString(SharedPreferencesManager.lastUserName, userName)
        SharedPreferencesManager.setInt(SharedPreferencesManager.userid, userId)
        mUser = getLastUser()
    }
}
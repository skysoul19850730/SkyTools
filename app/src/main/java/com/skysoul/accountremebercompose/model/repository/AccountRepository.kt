package com.skysoul.accountremebercompose.model.repository

import com.skysoul.accountremebercompose.data.dbroom.entities.SimpleAccount
import com.skysoul.accountremebercompose.model.SSResult
import com.skysoul.accountremebercompose.model.beans.Account
import kotlinx.coroutines.flow.Flow

/**
 *@author shenqichao
 *Created on 2022/9/8
 *@Description
 */
interface AccountRepository {
    fun getAccountAll(userId:Int,cateId:Int): Flow<List<SimpleAccount>>
    fun searchAccounts(userId:Int,key:String): Flow<List<SimpleAccount>>
    suspend fun addAccount(account:Account):SSResult<Long>


}
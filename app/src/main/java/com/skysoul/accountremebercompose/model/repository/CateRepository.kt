package com.skysoul.accountremebercompose.model.repository

import com.skysoul.accountremebercompose.data.dbroom.entities.SimpleAccount
import com.skysoul.accountremebercompose.model.SSResult
import com.skysoul.accountremebercompose.model.beans.Account
import com.skysoul.accountremebercompose.model.beans.Cate
import kotlinx.coroutines.flow.Flow

/**
 *@author shenqichao
 *Created on 2022/9/8
 *@Description
 */
interface CateRepository {
    suspend fun getCateAll(userId:Int): SSResult<Flow<List<Cate>>>
    suspend fun addCate(cate:Cate):SSResult<Long>
}
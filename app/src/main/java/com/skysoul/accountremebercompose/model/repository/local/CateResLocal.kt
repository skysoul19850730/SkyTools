package com.skysoul.accountremebercompose.model.repository.local

import com.skysoul.accountremebercompose.data.dbroom.AccountDatabase
import com.skysoul.accountremebercompose.data.dbroom.daos.CateDao
import com.skysoul.accountremebercompose.model.SSResult
import com.skysoul.accountremebercompose.model.api.BaseRepository
import com.skysoul.accountremebercompose.model.beans.Account
import com.skysoul.accountremebercompose.model.beans.Cate
import com.skysoul.accountremebercompose.model.repository.CateRepository
import com.skysoul.accountremebercompose.utils.log
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

/**
 *@author shenqichao
 *Created on 2022/9/8
 *@Description
 */
class CateResLocal() : CateRepository, BaseRepository() {
    private val errorMessage = "操作出现异常"
    private val dao: CateDao = AccountDatabase.getInstance().cateDao()

    private suspend fun <T : Any> safeDBCall(call: suspend () -> SSResult<T>?): SSResult<T> {
        return safeApiCall({
            coroutineScope {
                call()
            }
        }, errorMessage)
    }

    override suspend fun getCateAll(userId: Int): SSResult<Flow<List<Cate>>> {
        return safeDBCall {
            dao.getAllCaesByUserid(userId = userId)?.run {
                log("recive db cate all flow ")
                SSResult.Success(this.map {
                    it.map { dm ->
                        Cate(dm.id, dm.cateName, dm.userId)
                    }
                })
            }
        }
    }

    override suspend fun addCate(cate: Cate): SSResult<Long> {
        return safeDBCall {
            dao.insert(cate.toDMCate()).run{
                SSResult.Success(this)
            }
        }
    }

}
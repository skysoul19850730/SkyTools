package com.skysoul.accountremebercompose.model.repository.local

import com.skysoul.accountremebercompose.data.dbroom.AccountDatabase
import com.skysoul.accountremebercompose.data.dbroom.daos.AccountDao
import com.skysoul.accountremebercompose.data.dbroom.entities.AccountWithCate
import com.skysoul.accountremebercompose.data.dbroom.entities.DMAccount
import com.skysoul.accountremebercompose.data.dbroom.entities.SimpleAccount
import com.skysoul.accountremebercompose.model.SSResult
import com.skysoul.accountremebercompose.model.api.BaseRepository
import com.skysoul.accountremebercompose.model.beans.Account
import com.skysoul.accountremebercompose.model.repository.AccountRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow

/**
 *@author shenqichao
 *Created on 2022/9/8
 *@Description
 */
class AccountResLocal() : AccountRepository, BaseRepository() {
    private val errorMessage = "操作出现异常"
    private val dao: AccountDao = AccountDatabase.getInstance().accountDao()

    private suspend fun <T : Any> safeDBCall(call: suspend () -> SSResult<T>?): SSResult<T> {
        return safeApiCall({
            coroutineScope {
                call()
            }
        }, errorMessage)
    }

    fun getAccount(id: Int): AccountWithCate {
        return dao.getDetailsById(id)
    }

    suspend fun removeAccounts(list: List<Int>): SSResult<Boolean> {
        return safeDBCall {
            dao.delete(list.map {
                DMAccount(it)
            })
            SSResult.Success(true)
        }
    }


    override fun getAccountAll(userId: Int,cateId:Int): Flow<List<SimpleAccount>> {
        return dao.getSimpleAccounts(userId = userId,cateId)

    }

    override fun searchAccounts(userId: Int, key: String): Flow<List<SimpleAccount>> {
        return dao.searchAccounts(userId,key)
    }

    override suspend fun addAccount(account: Account): SSResult<Long> {
        return safeDBCall {
            var result = (if (account.id > 0) {
                dao.update(account.toDmAccount())
                0L
            } else {
                dao.insert(account.toDmAccount())
            })
            result?.run {
                SSResult.Success(this)
            }
        }
    }
}
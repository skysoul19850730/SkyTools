package com.skysoul.accountremebercompose.data.dbroom.daos

import androidx.room.*
import com.skysoul.accountremebercompose.data.dbroom.BaseDao
import com.skysoul.accountremebercompose.data.dbroom.entities.AccountWithCate
import com.skysoul.accountremebercompose.data.dbroom.entities.DMAccount
import com.skysoul.accountremebercompose.data.dbroom.entities.SimpleAccount
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao: BaseDao<DMAccount> {

    @Query("select id,platform,accountName,tip from account where userId = :userId and (:cateId = cateId or :cateId = 0)")
    fun getSimpleAccounts(userId:Int,cateId:Int):Flow<List<SimpleAccount>>

    @Transaction
    @Query("select * from account where id =:accountId")
    fun getDetailsById(accountId:Int): AccountWithCate


}
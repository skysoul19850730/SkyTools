package com.skysoul.accountremebercompose.data.dbroom.daos

import androidx.room.Dao
import androidx.room.Query
import com.skysoul.accountremebercompose.data.dbroom.BaseDao
import com.skysoul.accountremebercompose.data.dbroom.entities.DMExtraColumn

@Dao
interface ExtrasDao: BaseDao<DMExtraColumn> {

    @Query("select * from extra_column where accountId = :accountId")
    fun getExtrasByAccountId(accountId:Int):List<DMExtraColumn>
}
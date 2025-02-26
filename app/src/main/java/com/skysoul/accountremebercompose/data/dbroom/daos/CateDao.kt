package com.skysoul.accountremebercompose.data.dbroom.daos

import androidx.room.Dao
import androidx.room.Query
import com.skysoul.accountremebercompose.data.dbroom.BaseDao
import com.skysoul.accountremebercompose.data.dbroom.entities.DMCate
import kotlinx.coroutines.flow.Flow

@Dao
interface CateDao : BaseDao<DMCate> {

    @Query("select * from cate where userId = :userId")
    fun getAllCaesByUserid(userId: Int): Flow<List<DMCate>>

    @Query("select * from cate where id = :id")
    fun getCateById(id: Int): DMCate?

    @Query("select * from cate where userId = :userId and cateName = :name")
    fun getCateByName(userId: Int, name: String): DMCate?

}
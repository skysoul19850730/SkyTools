package com.skysoul.accountremebercompose.data.dbroom.daos

import androidx.room.Dao
import androidx.room.Query
import com.skysoul.accountremebercompose.data.dbroom.BaseDao
import com.skysoul.accountremebercompose.data.dbroom.entities.DMExtraColumn
import com.skysoul.accountremebercompose.data.dbroom.entities.DMMember
import com.skysoul.accountremebercompose.data.dbroom.entities.DMUser

@Dao
interface MemberDao: BaseDao<DMMember> {

    @Query("select * from user_member where userId = :userId")
    fun getMembersByUserId(userId:Int):List<DMMember>

    @Query("SELECT * from user_member where id=:id")
    suspend fun getMemberById(id: Int): DMMember?
}
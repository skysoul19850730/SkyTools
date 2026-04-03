package com.skysoul.accountremebercompose.data.dbroom.entities

import androidx.room.Embedded
import androidx.room.Relation

/**
 * User 及其关联的 Member 列表
 * 使用 @Embedded 和 @Relation 实现一对多关联查询
 */
data class UserWithMembers(
    @Embedded
    val user: DMUser,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val members: List<DMMember>,

    @Relation(
        parentColumn = "currentDMMemberId",
        entityColumn = "id"
    )
    val currentMember: DMMember?
) {
}
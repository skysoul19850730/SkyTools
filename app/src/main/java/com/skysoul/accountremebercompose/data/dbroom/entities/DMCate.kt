package com.skysoul.accountremebercompose.data.dbroom.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

//同一个用户不能有相同的分类名称，userId和cateName不能同时相同
@Entity(
    tableName = "cate",
    indices = [
        Index(value = ["userId", "cateName"], unique = true),
        Index("userId")
    ],
    foreignKeys = [
        ForeignKey(
            entity = DMUser::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = CASCADE
        )
    ]
)
data class DMCate(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var cateName: String = "",
    var userId: Int = 0
)
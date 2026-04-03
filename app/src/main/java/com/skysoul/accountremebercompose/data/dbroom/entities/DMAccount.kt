package com.skysoul.accountremebercompose.data.dbroom.entities

import androidx.compose.animation.fadeOut
import androidx.compose.runtime.mutableStateOf
import androidx.room.*
import com.skysoul.accountremebercompose.data.dbroom.converts.ExtraColumn
import com.skysoul.accountremebercompose.data.dbroom.converts.ExtraColumnConvert

@Entity(
    tableName = "account",
    foreignKeys = [
        ForeignKey(
            entity = DMCate::class,
            parentColumns = ["id"],
            childColumns = ["cateId"],
            onDelete = ForeignKey.SET_DEFAULT
        ),
        ForeignKey(
            entity = DMMember::class,
            parentColumns = ["id"],
            childColumns = ["memberId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("cateId"),
        Index("memberId"),

    ]
)
@TypeConverters(ExtraColumnConvert::class)
data class DMAccount(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var platform: String = "",
    var accountName: String = "",
    var password: String = "",
    var tip: String = "",
    var bindPhone: String = "",
    var bindMail: String = "",
    var createTime: String = "",
    var cateId: Int = 1,
    var memberId: Int = 0,
    var externalColumns: List<ExtraColumn> = arrayListOf()
)

data class SimpleAccount(
    val id: Int = 0,
    var platform: String = "",
    var accountName: String = "",
    var tip: String = "",

    )

data class AccountWithCate(
    @Embedded
    var account: DMAccount,
    @Relation(parentColumn = "cateId", entityColumn = "id")
    var cate: DMCate
    )
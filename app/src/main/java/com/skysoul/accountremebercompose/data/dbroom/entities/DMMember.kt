package com.skysoul.accountremebercompose.data.dbroom.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.skysoul.accountremebercompose.data.dbroom.entities.DMAccount
import java.sql.Blob

@Entity(
    tableName = "user_member",
    indices = [
        Index("userId")
    ],
    foreignKeys = [
        ForeignKey(
            entity = DMUser::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DMMember(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var userId: Int = 0,
    var nickName:String = "",
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var avatar: ByteArray?=null,
) {
}
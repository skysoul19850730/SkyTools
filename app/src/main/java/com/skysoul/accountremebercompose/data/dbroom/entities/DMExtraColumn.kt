package com.skysoul.accountremebercompose.data.dbroom.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.skysoul.accountremebercompose.data.dbroom.entities.DMAccount

@Entity(
    tableName = "extra_column",
    indices = [
        Index("accountId", "extraKey", unique = true),
        Index("accountId")
    ],
    foreignKeys = [
        ForeignKey(
            entity = DMAccount::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DMExtraColumn(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var accountId: Int = 0,
    var extraKey: String = "",
    var extraValue: String = ""
)
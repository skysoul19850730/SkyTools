package com.skysoul.accountremebercompose.model.beans

import android.os.Parcel
import android.os.Parcelable
import com.skysoul.accountremebercompose.data.dbroom.entities.DMCate
import com.skysoul.accountremebercompose.ui.dialog.ListDialogItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cate(
    var id: Int = 0,
    var cateName: String = "",
    var userId: Int = 0
) : Parcelable,ListDialogItem {
    fun toDMCate(): DMCate {
        return DMCate(id, cateName, userId)
    }

    companion object{
        fun fromDmCate(dmCate: DMCate):Cate{
            return dmCate.run {
                Cate(id, cateName, userId)
            }
        }
    }

    override fun getTitle(): String {
        return cateName
    }
}
package com.skysoul.accountremebercompose.data.dbroom.converts

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *@author shenqichao
 *Created on 2022/9/14
 *@Description type 0 普通，只需要key value。  1 图片地址
 */
@Parcelize
data class ExtraColumn(var key:String,var value:String,var type:Int =0) : Parcelable

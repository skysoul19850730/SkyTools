package com.skysoul.accountremebercompose.data.dbroom.converts

import android.text.TextUtils
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.skysoul.accountremebercompose.utils.MD5

class ExtraColumnConvert {
    companion object{

        @TypeConverter
        @JvmStatic
        fun toJsonString(extras: List<ExtraColumn>?):String{
            if(extras.isNullOrEmpty())return ""
            return Gson().toJson(extras)
        }

        @TypeConverter
        @JvmStatic
        fun toExtraColumns(str:String?): List<ExtraColumn>{
            var result = arrayListOf<ExtraColumn>()
            if(str.isNullOrEmpty())return result
            var listType = object :TypeToken<ArrayList<ExtraColumn>>(){}.type
            result = Gson().fromJson(str,listType)
            return result
        }
    }
}
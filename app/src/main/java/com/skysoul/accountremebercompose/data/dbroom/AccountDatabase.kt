package com.skysoul.accountremebercompose.data.dbroom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.skysoul.accountremebercompose.MApplication
import com.skysoul.accountremebercompose.data.dbroom.daos.AccountDao
import com.skysoul.accountremebercompose.data.dbroom.daos.CateDao
import com.skysoul.accountremebercompose.data.dbroom.daos.ExtrasDao
import com.skysoul.accountremebercompose.data.dbroom.daos.UserDao
import com.skysoul.accountremebercompose.data.dbroom.entities.DMAccount
import com.skysoul.accountremebercompose.data.dbroom.entities.DMCate
import com.skysoul.accountremebercompose.data.dbroom.entities.DMExtraColumn
import com.skysoul.accountremebercompose.data.dbroom.entities.DMUser
import com.skysoul.appassistant.ContextProvider

@Database(version = 1,exportSchema = false,
        entities = arrayOf(DMAccount::class, DMCate::class, DMUser::class, DMExtraColumn::class))
abstract class AccountDatabase:RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun cateDao(): CateDao
    abstract fun accountDao(): AccountDao
    abstract fun extrasDao(): ExtrasDao

    companion object{
        private var INSTANCE: AccountDatabase?=null
        var lock = Any()
        fun getInstance(): AccountDatabase {
            if(INSTANCE ==null){
                synchronized(lock){
                    if(INSTANCE ==null){
                        INSTANCE = Room.databaseBuilder(ContextProvider.context.applicationContext,
                                AccountDatabase::class.java,"account2.db").build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}
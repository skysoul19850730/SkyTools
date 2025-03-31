package com.skysoul.accountremebercompose

import android.app.Application
import android.content.Context
import com.skysoul.accountremebercompose.utils.log
import com.skysoul.appassistant.ActivityCallbackImpl
import com.skysoul.appassistant.ActivityStackManager
import com.skysoul.appassistant.ContextProvider

/**
 * Created by Administrator on 2018/4/4.
 */
class MApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ContextProvider.init(this)
    }



    fun exitProgram() {
        log("exitProgram：");
        try {
            ActivityStackManager.finishActivities()
            KillApp()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun KillApp() {
        android.os.Process.killProcess(android.os.Process.myPid())
    }

}
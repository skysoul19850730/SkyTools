package com.skysoul.appassistant

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 *@author shenqichao
 *Created on 2025/3/27
 *@Description
 */
@SuppressLint("StaticFieldLeak")
object ContextProvider {
    lateinit var context: Context


    fun init(app: Application) {
        app.registerActivityLifecycleCallbacks(ActivityCallbackImpl)
        context = app.applicationContext
    }
}
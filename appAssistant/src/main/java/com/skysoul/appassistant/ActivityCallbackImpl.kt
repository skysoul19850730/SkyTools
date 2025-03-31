package com.skysoul.appassistant

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.skysoul.utils.log.LogUtil
import com.skysoul.utils.log.NoLogTrace

/**
 *@author shenqichao
 *Created on 2025/3/26
 *@Description
 */
object ActivityCallbackImpl : Application.ActivityLifecycleCallbacks {

    private var resumeActivityCount = 0
    private var backgroudStayTime = 0L
    private  val TAG = "ActivityState"

    @NoLogTrace
    private fun log(msg:String){
        LogUtil.d(msg,TAG)
    }

    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
      log(activity.javaClass.getName() + ", " + activity.hashCode())
        ActivityStackManager.onCreate(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        log(activity.javaClass.getName() + ", " + activity.hashCode())
        if(resumeActivityCount == 0){
            ActivityStackManager.mStateChangeListenerList.forEach {
                it.appTurnIntoForeground()
            }
        }
        resumeActivityCount++
    }

    override fun onActivityResumed(activity: Activity) {
        log(activity.javaClass.getName() + ", " + activity.hashCode())
        ActivityStackManager.onResume(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        log(activity.javaClass.getName() + ", " + activity.hashCode())
        ActivityStackManager.onPause(activity)

    }

    override fun onActivityStopped(activity: Activity) {
        log(activity.javaClass.getName() + ", " + activity.hashCode())
        resumeActivityCount--
        if(resumeActivityCount == 0){
            backgroudStayTime = System.currentTimeMillis()
            ActivityStackManager.mStateChangeListenerList.forEach {
                it.appTurnIntoBackGround()
            }
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
        log(activity.javaClass.getName() + ", " + activity.hashCode())
    }

    override fun onActivityDestroyed(activity: Activity) {
        log(activity.javaClass.getName() + ", " + activity.hashCode())
        ActivityStackManager.onDestroy(activity)
    }
}
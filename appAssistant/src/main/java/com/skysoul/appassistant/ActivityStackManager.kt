package com.skysoul.appassistant

import android.annotation.SuppressLint
import android.app.Activity
import java.util.Deque

/**
 *@author shenqichao
 *Created on 2025/3/27
 *@Description
 */
@SuppressLint("StaticFieldLeak")
object ActivityStackManager {

    interface AppStateChangeListener{
        fun appTurnIntoForeground()
        fun appTurnIntoBackGround()
    }

    private val mActivities: Deque<Activity> = java.util.ArrayDeque()

    private var mCurrentActivity: Activity? = null
    private var mLastActivity: Activity? = null
    private var forgroundTime = 0L

    var isBackground = true
    val mStateChangeListenerList = arrayListOf<AppStateChangeListener>(
        object : AppStateChangeListener{
            override fun appTurnIntoForeground() {
                isBackground = false
                forgroundTime =
                    System.currentTimeMillis() / 1000
            }

            override fun appTurnIntoBackGround() {
                isBackground = true
                val time = System.currentTimeMillis() / 1000
            }
        }
    )

    fun onCreate(activity: Activity) {
        mActivities.addLast(activity)
    }
    fun onDestroy(activity: Activity) {
        if (mLastActivity === activity) {
            mLastActivity = null
        }
        mActivities.remove(activity)
    }

    fun finishActivities() {
        for (activity in mActivities) {
            activity.finish()
        }
        mActivities.clear()
        System.exit(0)
    }

    fun onResume(activity: Activity) {
        mCurrentActivity = activity
    }

    fun onPause(activity: Activity) {
        mCurrentActivity = null
        mLastActivity = activity
    }

    fun getCurrentActivity(): Activity {
        return mCurrentActivity!!
    }

    fun getAvailableActivity(): Activity {
        return if (mCurrentActivity != null) mCurrentActivity!!
        else mLastActivity!!
    }
    fun isCurrentActivity(activity: Activity): Boolean {
        return mCurrentActivity === activity
    }
    fun getActivities(): Deque<Activity> {
        return mActivities
    }


}
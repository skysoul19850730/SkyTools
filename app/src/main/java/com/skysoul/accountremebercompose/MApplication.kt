package com.skysoul.accountremebercompose

import android.app.Application
import android.graphics.BitmapFactory
import android.widget.ImageView
import com.skysoul.accountremebercompose.utils.log
import com.skysoul.appassistant.ActivityStackManager
import com.skysoul.appassistant.ContextProvider
import com.skysoul.album.AlbumManager
import com.skysoul.album.ImageLoaderInterface
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created by Administrator on 2018/4/4.
 */
class MApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ContextProvider.init(this)
        AlbumManager.init(this,"","",object : ImageLoaderInterface{
            override fun loadImage(p0: ImageView, p1: String) {
                GlobalScope.launch {
                    val bitmap = BitmapFactory.decodeFile(p1)
                    //切到main
                    p0.post {
                        p0.setImageBitmap(bitmap)
                    }
                }
            }

        })
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
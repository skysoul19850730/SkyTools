package com.skysoul.utils

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.skysoul.appassistant.ActivityStackManager
import com.skysoul.appassistant.ContextProvider
import com.skysoul.appassistant.R

/**
 *@author shenqichao
 *Created on 2025/3/28
 *@Description
 */
object ToastUtil {

    fun showToast(text: String?, duration:Int = Toast.LENGTH_SHORT,gravity: Int = Gravity.BOTTOM){
        doShowToast(text,duration,gravity)
    }
    fun showIconToastLr(text: String?,icon:Int,duration:Int = Toast.LENGTH_SHORT,gravity: Int = Gravity.BOTTOM){
        showToastWithIcon(text,R.layout.toast_lr,icon,duration,gravity)
    }
    fun showIconToastUd(text: String?,icon:Int,duration:Int = Toast.LENGTH_SHORT,gravity: Int = Gravity.BOTTOM){
        showToastWithIcon(text,R.layout.toast_ud,icon,duration,gravity)
    }

    fun showViewToast(view: View?, duration:Int = Toast.LENGTH_SHORT,gravity: Int = Gravity.BOTTOM){
        doShowToast(null,duration,gravity,view)
    }

    fun showComposeToast(duration:Int = Toast.LENGTH_SHORT, gravity: Int = Gravity.BOTTOM, viewContent :@Composable () -> Unit){

        val view = ComposeView(ContextProvider.context)
        val act = ActivityStackManager.getCurrentActivity() as FragmentActivity
        view.setViewTreeLifecycleOwner(act)
        view.setViewTreeViewModelStoreOwner(act)
        view.setViewTreeSavedStateRegistryOwner(act)
        view.setContent {

            viewContent()
        }
        doShowToast(null,duration,gravity,view)

    }


    private fun showToastWithIcon(text: String?,resId:Int,icon:Int,duration:Int = Toast.LENGTH_SHORT,gravity: Int = Gravity.BOTTOM){

        val view = LayoutInflater.from(ContextProvider.context).inflate(resId,null)
        val textView = view.findViewById<TextView>(R.id.tv_toast)
        val iconView = view.findViewById<ImageView>(R.id.img_toast)
        textView.text = text
        iconView.setImageResource(icon)
        doShowToast(text,duration,gravity,view)
    }


    private fun doShowToast(text:String?, duration:Int,gravity: Int = Gravity.BOTTOM,view: View?=null){
        if(text.isNullOrEmpty() && view== null)return
        if(ActivityStackManager.isBackground){
            return
        }
        ThreadManager.getManager().runOnUIThread {
            val toast = Toast.makeText(ContextProvider.context,text,duration)
            if(view!=null){
                toast.view = view
            }

            toast.setGravity(gravity,0,0)
            toast.duration = duration
            try {
                toast.show()
            }catch (e:Exception){
                if(!text.isNullOrEmpty()){
                    ToastCompat.makeText(ContextProvider.context,text,duration)
                }
            }
        }


    }


}
package com.skysoul.utils.log

import android.util.Log

/**
 *@author shenqichao
 *Created on 2025/3/26
 *@Description
 */
object LogUtil {
    const val TAG = "LogUtil"
    var openLog :Any?=1
    var useStaceInfo = true

    @NoLogTrace
    fun d(msg:String,tag:String?=null){
      traceLog(msg,tag,"d")
    }
    @NoLogTrace
    fun i(msg:String,tag:String?=null){
        traceLog(msg,tag,"i")
    }
    @NoLogTrace
    fun e(msg:String,tag:String?=null){
        traceLog(msg,tag,"e")
    }
    @NoLogTrace
    fun v(msg:String,tag:String?=null){
        traceLog(msg,tag,"v")
    }
    @NoLogTrace
    fun w(msg:String,tag:String?=null){
        traceLog(msg,tag,"w")
    }



    @NoLogTrace
    private fun traceLog(msg:String,tag: String?,level:String){
        openLog?:return
        var pre = ""

        if(useStaceInfo) {
            val sts = Throwable().stackTrace
            var st = sts.firstOrNull {
                Class.forName(it.className).declaredMethods.firstOrNull {m->
                    m.name == it.methodName }?.getAnnotation(NoLogTrace::class.java) != null
            }
            if(st == null){//找不到的话，就只定位第一个不是LogUtil的类
                st = sts.firstOrNull{
                    it.className != LogUtil::class.java.name
                }
            }


            if (st != null) {
                var name = st.className.substring(st.className.lastIndexOf(".") + 1)
                    .substringBeforeLast("$")
                pre = "${name}->${st.methodName}():"
            }
        }

        var t = TAG
        if(!tag.isNullOrEmpty()){
            t+=" $tag"
        }

        when(level){
            "d"->{
                Log.d(t, "${pre}$msg")
            }
            "v"->{
                Log.v(t, "${pre}$msg")
            }
            "e"->{
                Log.e(t, "${pre}$msg")
            }
            "i"->{
                Log.i(t, "${pre}$msg")
            }
            "w"->{
                Log.w(t, "${pre}$msg")
            }

        }

    }

}
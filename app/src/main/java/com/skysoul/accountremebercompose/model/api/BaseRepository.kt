package com.skysoul.accountremebercompose.model.api

import java.io.IOException
import com.skysoul.accountremebercompose.model.SSResult

/**
 * Created by luyao
 * on 2019/4/10 9:41
 */
open class BaseRepository {

    protected suspend fun <T : Any> safeApiCall(call: suspend () -> SSResult<T>?, errorMessage: String = "操作异常"): SSResult<T> {
        return try {
            call()?: SSResult.Error(NilException())
        } catch (e: Exception) {
            // An exception was thrown when calling the API so we're converting this to an IOException
            SSResult.Error(IOException("$errorMessage : ${e.message}", e))
        }
    }

}

class NilException : Exception(){

}
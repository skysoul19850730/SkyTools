package com.skysoul.accountremebercompose.model

/**
 * Created by luyao
 * on 2019/10/12 11:08
 */
sealed class SSResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : SSResult<T>()
    data class Error(val exception: Exception) : SSResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }

    inline fun ifSuccess(block: (T) -> Unit): SSResult<T> {
        if (this is Success) {
            block.invoke(data)
        }
        return this
    }

    inline fun ifError(block: (e: Exception) -> Unit): SSResult<T> {
        if (this is Error) {
            block.invoke(exception)
        }
        return this
    }
}
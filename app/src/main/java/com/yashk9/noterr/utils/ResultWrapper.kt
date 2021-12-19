package com.yashk9.noterr.utils

import kotlin.Exception

sealed class Result<out V, out E>(){
    data class Success<out V>(val value: V): Result<V, Nothing>()
    data class Error<out E>(val exception: E): Result<Nothing, E>()

    companion object factory{
        inline fun <V> build(function: () -> V): Result<V, Exception> =
            try {
                Success(function.invoke())
            } catch (e: Exception) {
                Error(e)
            }
    }
}
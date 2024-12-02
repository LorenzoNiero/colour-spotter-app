package com.challenge.colour_spotter.common.domain

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val throwable: Throwable) : Result<Nothing>()
}

suspend fun <T> getResult(onError: (throwable: Throwable) -> Unit = {}, invoke: suspend () -> T): Result<T> {
    return runCatching {
        Result.Success(invoke())
    }.getOrElse {
        onError(it)
        Result.Error(it)
    }
}

fun <T> getResultBlocking(onError: (throwable: Throwable) -> Unit = {}, invoke:  () -> T): Result<T> {
    return try {
        Result.Success(invoke())
    } catch( ex : Exception) {
        onError(ex)
        Result.Error(ex)
    }
}

inline fun <T> Result<T>.onError(block: (Throwable) -> Unit): Result<T> {
    if (this is Result.Error) {
        block(throwable)
    }
    return this
}

inline fun <T> Result<T>.onSuccess(block: (T) -> Unit): Result<T> {
    if (this is Result.Success) {
        block(this.data)
    }
    return this
}

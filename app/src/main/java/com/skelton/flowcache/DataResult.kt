package com.skelton.flowcache

sealed class DataResult<out T : Any> {
    sealed class Success<T : Any> : DataResult<T>() {
        abstract val data: T

        data class Cache<T : Any>(override val data: T) : Success<T>()
        data class Network<T : Any>(override val data: T) : Success<T>()
    }

    data class Error(
        val errorMessage: String,
        val errorCode: Code = Code.None
    ) : DataResult<Nothing>() {
        enum class Code {
            NotFound, NotAuthorized, Blocked, None
        }
    }
}
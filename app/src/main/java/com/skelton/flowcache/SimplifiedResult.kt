package com.skelton.flowcache.simple

sealed class Result<out T : Any> {
    sealed class Success<T : Any> : Result<T>() {
        abstract val data: T

        data class Cache<T : Any>(override val data: T) : Success<T>()
        data class Network<T : Any>(override val data: T) : Success<T>()
    }

    data class Error(val errorMessage: String) : Result<Nothing>()
}


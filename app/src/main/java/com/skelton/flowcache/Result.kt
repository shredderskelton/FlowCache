package com.skelton.flowcache

sealed class Result<out T : Any> {
    sealed class Success<T : Any> : Result<T>() {
        abstract val data: T

        class Cached<T : Any>(override val data: T) : Success<T>()
        class Network<T : Any>(override val data: T) : Success<T>()
    }

    data class Error(val errorMessage: String) : Result<Nothing>()
}
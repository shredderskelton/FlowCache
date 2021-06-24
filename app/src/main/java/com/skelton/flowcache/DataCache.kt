package com.skelton.flowcache

import kotlinx.coroutines.flow.flow

interface DataCache {
    suspend fun <T : Any> get(key: String): T?
    fun <T : Any> set(key: String, value: T, timeout: CachePolicy.Timeout)
    fun clear(key: String)
    fun clearAll()
}

inline fun <reified T : Any> DataCache.createCachedFlow(
    key: String,
    policy: CachePolicy,
    crossinline block: suspend () -> Result<T>,
) = flow {
    var cacheHit = false

    // Check the Cache first
    val cachedResult = get<T>(key)
    if (cachedResult != null) {
        println("$key Cache hit")
        cacheHit = true
        emit(Result.Success.Cache(cachedResult))
    } else {
        println("$key Cache miss")
    }

    // Then go to the source, in this case the "Network"
    when (val result = block()) {
        is Result.Success -> {
            println("$key Success from Network, storing in Cache and emitting")
            set(key, result.data, policy.time)
            emit(result)
        }
        is Result.Error -> {
            // If we emitted a cached result, then we need to decide whether
            // to follow up with an error as well.
            if (!cacheHit) {
                println("$key Error from Network and because there was nothing in the cache, were are emitting the error")
                emit(result)
            } else if (result.shouldNotify(policy.errorFilter)) {
                println(
                    "$key Error from Network, and since there was already something emitting from the cache," +
                        " we checked the Cache policy and determined that we should emit this error: ${result.errorMessage}"
                )
                emit(result)
            } else {
                println(
                    "$key Error from Network, and since there was already something emitting from the cache," +
                        " we checked the Cache policy and determined that we should not emit this error: ${result.errorMessage}"
                )
            }
        }
    }
}

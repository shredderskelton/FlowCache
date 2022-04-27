package com.skelton.flowcache.cache

import com.skelton.flowcache.DataResult
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
    crossinline block: suspend () -> DataResult<T>,
) = flow {
    var cacheHit = false

    // 1. Check the Cache
    val cachedResult = get<T>(key)
    if (cachedResult != null) {
        cacheHit = true
        emit(DataResult.Success.Cache(cachedResult))
    }

    // 2. Then go to the source, in this case the "Network"
    when (val result = block()) {
        is DataResult.Success -> {
            set(key, result.data, policy.time)
            emit(result)
        }
        is DataResult.Error ->
            if (!cacheHit || result.shouldNotify(policy.errorFilter))
                emit(result)
    }
}

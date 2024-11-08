package com.skelton.flowcache.account

import com.skelton.flowcache.DataResult
import com.skelton.flowcache.cache.CachePolicy
import com.skelton.flowcache.cache.DataCache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.Duration

/**
 * This is a very short delegate that queues up all incoming requests to endpoints
 * only allowing the first request through to the network.
 *
 * All subsequent requests wait behind the Mutex for the first to complete.
 *
 * If the first is successful, it will leave it's result in the cache allowing the
 * queued requests to avoid the network altogether.
 *
 * This solves the main problem of Cold Flows - multiple flows hitting the same source
 * at the same time. A simple and short cache debounces multiple incoming identical flows,
 * essentially 'sharing' them.
 *
 * Why not use a shareIn operator?
 *
 *  - This requires injecting an external scope into the DataSources
 *  - Complicates testing
 *  - Forces the cache to be in memory (right now, the DataCache is in memory, but could potentially
 *    be in a database or filesystem)
 *
 */
class AccountApiCached(
    private val cache: DataCache,
    private val realDataSource: AccountApi
) : AccountApi by realDataSource {
    override suspend fun getAccountDetails(id: String): DataResult<AccountEntity> =
        cache.getOrPut(
            key = "account/$id",
            policy = CachePolicy.Timeout.MaxAge(Duration.ofMillis(100))
        ) { realDataSource.getAccountDetails(id) }
}

val mutexes: MutableMap<String, Mutex> = mutableMapOf()

/**
 * A coroutine safe way to request data. Only one coroutine may access
 * a @key at a time (other requests are queued) and cache misses will
 * be sent to the @source and successful trips to the @source will be
 * saved into the cache.
 */
suspend inline fun <reified T : Any> DataCache.getOrPut(
    key: String,
    policy: CachePolicy.Timeout,
    source: () -> DataResult<T>,
): DataResult<T> =
    mutexes.getOrPut(key) { Mutex() }.withLock {
        get<T>(key)?.let {
            DataResult.Success.Cache(it)
        } ?: source().also {
            if (it is DataResult.Success.Network)
                set(key, it.data, policy)
        }
    }

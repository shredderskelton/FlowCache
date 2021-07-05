package com.skelton.flowcache.cache

import com.skelton.flowcache.system.TimeProvider
import java.time.Duration

/**
 * This cache implementation will only survive as long as the Application process
 *
 * No persistence
 */
class DataCacheMemory(private val timeProvider: TimeProvider) : DataCache {

    private val cache = mutableMapOf<String, CacheEntry>()

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T : Any> get(key: String): T? {
        val entry = cache[key]
        return if (entry == null) {
            key.log("no cache entry")
            null
        } else {
            val timeToLive = Duration.between(timeProvider.now(), entry.expiry)
            if (timeToLive.isNegative) {
                key.log("entry expired ${timeToLive.seconds} seconds ago")
                null
            } else {
                key.log("cache hit: time to live ${timeToLive.seconds} seconds")
                entry.data as T
            }
        }
    }

    override fun <T : Any> set(key: String, value: T, timeout: CachePolicy.Timeout) {
        val expiryDuration = when (timeout) {
            is CachePolicy.Timeout.MaxAge -> timeout.duration
            is CachePolicy.Timeout.PointInTime -> {
                if (timeout.time.isBefore(timeProvider.now()))
                    key.log("Friendly warning: This cache entry has expired before it had a chance to live!")
                Duration.between(timeProvider.now(), timeout.time)
            }
            CachePolicy.Timeout.Always -> Duration.ZERO
            CachePolicy.Timeout.Never -> Duration.ofDays(Long.MAX_VALUE)
        }
        val entry = CacheEntry(value, timeProvider.now() + expiryDuration)
        key.log("Setting cache Entry: $entry")
        cache[key] = entry
    }

    override fun clear(key: String) {
        cache.remove(key)
    }

    override fun clearAll() {
        cache.clear()
    }

    private fun String.log(message:String){
        println("$this: $message")
    }
}
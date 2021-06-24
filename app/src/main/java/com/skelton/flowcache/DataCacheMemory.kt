package com.skelton.flowcache

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
            println("$key no entry")
            null
        } else {
            val timeToLive = Duration.between(timeProvider.now(), entry.expiry)
            if (timeToLive.isNegative) {
                println("$key entry expired ${timeToLive.seconds} seconds ago")
                null
            } else {
                println("$key cache hit: time to live ${timeToLive.seconds} seconds")
                entry.data as T
            }
        }
    }

    override fun <T : Any> set(key: String, value: T, timeout: CachePolicy.Timeout) {
        val expiryDuration = when (timeout) {
            is CachePolicy.Timeout.MaxAge -> timeout.duration
            is CachePolicy.Timeout.PointInTime -> {
                if (timeout.time.isBefore(timeProvider.now()))
                    println("Friendly warning: This cache entry has expired before it had a chance to live!")
                Duration.between(timeProvider.now(), timeout.time)
            }
            CachePolicy.Timeout.Always -> Duration.ZERO
            CachePolicy.Timeout.Never -> Duration.ofDays(Long.MAX_VALUE)
            else -> Duration.ZERO
        }
        val entry = CacheEntry(value, timeProvider.now() + expiryDuration)
        println("$key Setting cache Entry: $entry")
        cache[key] = entry
    }

    override fun clear(key: String) {
        cache.remove(key)
    }

    override fun clearAll() {
        cache.clear()
    }
}
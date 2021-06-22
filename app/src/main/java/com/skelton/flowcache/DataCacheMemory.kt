package com.skelton.flowcache

import java.time.Duration

import java.time.Instant

data class CacheEntry(val data: Any, val expiry: Instant)

interface TimeProvider {
    fun now(): Instant
}

internal class TimeProviderImpl : TimeProvider {
    override fun now(): Instant = Instant.now()
}

/**
 * This cache implementation will only survive as long as the Application process
 *
 * No persistence
 */
class DataCacheMemory(private val timeProvider: TimeProvider) : DataCache {

    private val cache = mutableMapOf<String, CacheEntry>()

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T : Any> get(key: String): T? {
        return cache[key]?.let {
            if (it.expiry > timeProvider.now())
                it.data as T
            else null
        }
    }

    override fun <T : Any> set(key: String, value: T, expiryDuration: Duration) {
        cache[key] = CacheEntry(value, timeProvider.now() + expiryDuration)
    }

    override fun clear(key: String) {
        cache.remove(key)
    }

    override fun clearAll() {
        cache.clear()
    }
}
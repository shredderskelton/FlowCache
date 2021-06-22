package com.skelton.flowcache

import java.time.Duration

/**
 * No caching
 */
object DataCacheNoOp: DataCache {
    override suspend fun <T : Any> get(key: String): T?  = null
    override fun <T : Any> set(key: String, value: T, expiryDuration: Duration)  = Unit
    override fun clear(key: String)  = Unit
    override fun clearAll() = Unit
}
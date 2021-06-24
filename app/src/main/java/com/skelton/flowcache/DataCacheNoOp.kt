package com.skelton.flowcache

/**
 * No caching
 */
object DataCacheNoOp : DataCache {
    override suspend fun <T : Any> get(key: String): T? = null
    override fun <T : Any> set(key: String, value: T, timeout: CachePolicy.Timeout) = Unit
    override fun clear(key: String) = Unit
    override fun clearAll() = Unit
}
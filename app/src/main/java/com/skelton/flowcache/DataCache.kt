package com.skelton.flowcache

import java.time.Duration

interface DataCache {
    suspend fun <T : Any> get(key: String): T?
    fun <T : Any> set(key: String, value: T, expiryDuration: Duration)
    fun clear(key: String)
    fun clearAll()
}


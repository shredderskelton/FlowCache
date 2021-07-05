package com.skelton.flowcache.cache

import java.time.Instant

data class CacheEntry(val data: Any, val expiry: Instant)
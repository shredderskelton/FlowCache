package com.skelton.flowcache

import java.time.Instant

data class CacheEntry(val data: Any, val expiry: Instant)
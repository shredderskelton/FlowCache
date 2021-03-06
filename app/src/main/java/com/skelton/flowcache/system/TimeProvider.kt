package com.skelton.flowcache.system

import java.time.Instant

interface TimeProvider {
    fun now(): Instant
}

internal class TimeProviderImpl : TimeProvider {
    override fun now(): Instant = Instant.now()
}
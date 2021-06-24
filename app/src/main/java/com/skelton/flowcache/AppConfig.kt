package com.skelton.flowcache

import java.time.Duration

data class AppConfig(
    val simulatedNetworkDelay: Duration = Duration.ofSeconds(6),
    val staleDataAge: Duration = Duration.ofSeconds(10)
)
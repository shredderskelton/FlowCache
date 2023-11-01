package com.skelton.flowcache

import java.time.Duration

data class AppConfig(
    val simulatedNetworkDelay: Duration = Duration.ofSeconds(2),
    val maximumDataAge: Duration = Duration.ofSeconds(6)
)
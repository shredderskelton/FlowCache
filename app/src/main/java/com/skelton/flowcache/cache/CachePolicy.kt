package com.skelton.flowcache.cache

import com.skelton.flowcache.DataResult
import java.time.Duration
import java.time.Instant

data class CachePolicy(
    val time: Timeout = Timeout.Always,
    val errorFilter: ErrorFilter = ErrorFilter.None
) {

    sealed class ErrorFilter {
        object None : ErrorFilter()
        object All : ErrorFilter()
        class Notify(val notifyOn: (DataResult.Error) -> Boolean) : ErrorFilter()
    }

    sealed class Timeout {
        object Never : Timeout() // Better name? Forever, NoExpiry...
        object Always : Timeout() // Better name? NoCache, Immediate...
        data class MaxAge(val duration: Duration = Duration.ofMinutes(5)) : Timeout()
        data class PointInTime(val time: Instant) : Timeout()
    }
}

fun DataResult.Error.shouldNotify(filter: CachePolicy.ErrorFilter) = when (filter) {
    CachePolicy.ErrorFilter.All -> true
    CachePolicy.ErrorFilter.None -> false
    is CachePolicy.ErrorFilter.Notify -> filter.notifyOn(this)
}

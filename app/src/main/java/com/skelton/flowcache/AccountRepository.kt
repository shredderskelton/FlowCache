package com.skelton.flowcache

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import java.time.Duration

interface AccountRepository {
    val accountDetails: Flow<Result<AccountDetails>>
    fun refresh()
}

class InMemoryAccountRepository(
    private val cache: DataCache,
    private val dataSource: AccountDataSource
) : AccountRepository {

    private val refreshTrigger = MutableStateFlow(0L)

    override val accountDetails = createFlow("Account") { dataSource.getAccountDetails() }

    override fun refresh() {
        refreshTrigger.value++
    }

    // Assumes the caching rules:
    // - One minute cache
    // - If there is a cache hit, emit the hit,
    //   and emit again if the network result is successful.
    // - If we have a cache hit, emit the hit, and ignore any errors that come afterward.
    //
    //   This is a little dangerous and different errors should be handled differently.
    //   Eg. This is ok if there was a Network failure (connection problem) but maybe not
    //   if there was a certain API error (eg. user has been deleted/banned/blocked)
    private inline fun <reified T : Any> createFlow(
        key: String,
        crossinline block: suspend () -> Result<T>,
    ): Flow<Result<T>> =
        refreshTrigger.flatMapConcat {
            flow {
                var cacheHit = false
                cache.get<T>(key)?.let {
                    cacheHit = true
                    println("Cache hit for $key!")
                    emit(Result.Success.Cached(it))
                }
                when (val result = block()) {
                    is Result.Success -> {
                        cache.set(key, result.data, Duration.ofMinutes(5))
                        emit(result)
                    }
                    is Result.Error -> {
                        // If we emitted a cached result, then don't send an error too
                        if (!cacheHit) emit(result)
                    }
                }
            }
        }
}


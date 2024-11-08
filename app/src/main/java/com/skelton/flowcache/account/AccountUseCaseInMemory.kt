package com.skelton.flowcache.account

import com.skelton.flowcache.DataResult
import com.skelton.flowcache.cache.CachePolicy
import com.skelton.flowcache.cache.DataCache
import com.skelton.flowcache.cache.createCachedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.Duration

class AccountUseCaseInMemory(
    private val cache: DataCache,
    private val api: AccountApi
) : AccountUseCase {

    override val isRefreshing = MutableStateFlow(false)

    override fun get(name: String, force: Boolean): Flow<DataResult<AccountEntity>> {
        if (force) cache.clearAll()
        return cache.createCachedFlow(
            key = "Account/$name",
            policy = CachePolicy(
                time = CachePolicy.Timeout.MaxAge(Duration.ofMinutes(20)),
                errorFilter = CachePolicy.ErrorFilter.Notify {
                    it.errorCode == DataResult.Error.Code.NotFound
                }
            )
        ) {
            isRefreshing.value = true
            api.getAccountDetails(name).also {
                isRefreshing.value = false
            }
        }
    }

    override suspend fun create(name: String, details: AccountEntity): DataResult<Unit> {
        val result = api.createAccount(name, details)
        // Write operation on cache
        if (result is DataResult.Success) {
            cache.set(
                "Account/$name",
                result.data,
                CachePolicy.Timeout.MaxAge(Duration.ofMinutes(1))
            )
        }
        return result
    }
}
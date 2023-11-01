package com.skelton.flowcache.account

import com.skelton.flowcache.DataResult
import com.skelton.flowcache.cache.CachePolicy
import com.skelton.flowcache.cache.DataCache
import com.skelton.flowcache.cache.createCachedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.Duration

class AccountRepositoryInMemory(
    private val cache: DataCache,
    private val dataSource: AccountDataSource
) : AccountRepository {

    override val isRefreshing = MutableStateFlow(false)
    override fun getAccount(name: String, force: Boolean): Flow<DataResult<AccountData>> {
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
            dataSource.getAccountDetails(name).also {
                isRefreshing.value = false
            }
        }
    }

    override suspend fun createAccount(name: String, details: AccountData): DataResult<Unit> {
        val result = dataSource.createAccount(name, details)
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
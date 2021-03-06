package com.skelton.flowcache.account

import com.skelton.flowcache.DataResult
import com.skelton.flowcache.account.AccountDataSource
import com.skelton.flowcache.account.AccountDetails
import com.skelton.flowcache.cache.CachePolicy
import com.skelton.flowcache.cache.DataCache
import com.skelton.flowcache.cache.createCachedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import java.time.Duration

interface AccountRepository {
    fun getAccount(name: String, force: Boolean = false): Flow<DataResult<AccountDetails>>
    suspend fun createAccount(name: String, details: AccountDetails): DataResult<Unit>
}

class InMemoryAccountRepository(
    private val cache: DataCache,
    private val dataSource: AccountDataSource
) : AccountRepository {

    override fun getAccount(name: String, force: Boolean): Flow<DataResult<AccountDetails>> {
        if (force) cache.clearAll()
        return cache.createCachedFlow(
            key = "Account/$name",
            policy = CachePolicy(
                time = CachePolicy.Timeout.MaxAge(Duration.ofMinutes(20)),
                errorFilter = CachePolicy.ErrorFilter.Notify {
                    it.errorCode == DataResult.Error.Code.NotFound
                }
            )
        ) { dataSource.getAccountDetails(name) }
    }

    override suspend fun createAccount(name: String, details: AccountDetails): DataResult<Unit> {
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


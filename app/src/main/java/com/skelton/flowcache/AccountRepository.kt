package com.skelton.flowcache

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import java.time.Duration

interface AccountRepository {
    fun getAccount(name: String, force: Boolean = false): Flow<Result<AccountDetails>>
    val isLoading: Flow<Boolean>
    suspend fun createAccount(name: String, details: AccountDetails): Result<Unit>
}

class InMemoryAccountRepository(
    private val cache: DataCache,
    private val dataSource: AccountDataSource
) : AccountRepository {

    override val isLoading = MutableStateFlow(false)

    override fun getAccount(name: String, force: Boolean): Flow<Result<AccountDetails>> {
        if (force) cache.clearAll()
        return cache.createCachedFlow(
            key = "Account/$name",
            policy = CachePolicy(
                CachePolicy.Timeout.MaxAge(Duration.ofMinutes(20)),
                CachePolicy.ErrorFilter.Notify {
                    it.errorCode == Result.Error.Code.NotFound
                }
            )
        ) { dataSource.getAccountDetails(name) }
            .onStart { isLoading.value = true }
            .onEach { isLoading.value = false }
    }

    override suspend fun createAccount(name: String, details: AccountDetails): Result<Unit> {
        val result = dataSource.createAccount(name, details)
        // Write operation on cache
        if (result is Result.Success) {
            cache.set(
                "Account/$name",
                result.data,
                CachePolicy.Timeout.MaxAge(Duration.ofMinutes(1))
            )
        }
        return result
    }
}


class DefaultAccountRepository(
    private val dataSource: AccountDataSource
) : AccountRepository {
    override val isLoading = MutableStateFlow(false)

    override fun getAccount(name: String, force: Boolean): Flow<Result<AccountDetails>> =
        flow { emit(dataSource.getAccountDetails(name)) }
            .onStart { isLoading.value = true }
            .onEach { isLoading.value = false }

    override suspend fun createAccount(name: String, details: AccountDetails): Result<Unit> {
        TODO("Not yet implemented")
    }
}

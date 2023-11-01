package com.skelton.flowcache.account

import com.skelton.flowcache.DataResult
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    val isRefreshing:Flow<Boolean>
    fun getAccount(name: String, force: Boolean = false): Flow<DataResult<AccountData>>
    suspend fun createAccount(name: String, details: AccountData): DataResult<Unit>
}


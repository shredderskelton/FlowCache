package com.skelton.flowcache.account

import com.skelton.flowcache.DataResult
import kotlinx.coroutines.flow.Flow

interface AccountUseCase {
    val isRefreshing:Flow<Boolean>
    fun get(name: String, force: Boolean = false): Flow<DataResult<AccountEntity>>
    suspend fun create(name: String, details: AccountEntity): DataResult<Unit>
}


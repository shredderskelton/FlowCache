package com.skelton.flowcache.account

import com.skelton.flowcache.DataResult

interface AccountApi {
    suspend fun getAccountDetails(id: String): DataResult<AccountEntity>
    suspend fun createAccount(id: String, details: AccountEntity): DataResult<Unit>
}


package com.skelton.flowcache.account

import com.skelton.flowcache.DataResult

interface AccountDataSource {
    suspend fun getAccountDetails(name: String): DataResult<AccountDetails>
    suspend fun createAccount(name: String, details: AccountDetails): DataResult<Unit>
}
package com.skelton.flowcache.account

import com.skelton.flowcache.DataResult

interface AccountDataSource {
    suspend fun getAccountDetails(id: String): DataResult<AccountData>
    suspend fun createAccount(id: String, details: AccountData): DataResult<Unit>
}
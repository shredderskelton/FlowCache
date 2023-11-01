package com.skelton.flowcache.account

import com.skelton.flowcache.DataResult
import com.skelton.flowcache.system.HttpProvider
import com.skelton.flowcache.system.request
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.encodedPath

class AccountDataSourceRest(
    private val httpProvider: HttpProvider
) : AccountDataSource {
    private fun buildUrl(id: String) = "/users/${id.lowercase()}.json"
    override suspend fun getAccountDetails(id: String): DataResult<AccountData> =
        httpProvider().request<AccountData> {
            method = HttpMethod.Get
            url {
                encodedPath = buildUrl(id)
            }
        }

    override suspend fun createAccount(id: String, details: AccountData): DataResult<Unit> =
        httpProvider().request<Unit> {
            method = HttpMethod.Put
            url {
                encodedPath = buildUrl(id)
            }
            setBody(details)
        }

}
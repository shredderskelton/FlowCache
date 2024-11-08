package com.skelton.flowcache.account

import com.skelton.flowcache.DataResult
import com.skelton.flowcache.system.HttpProvider
import com.skelton.flowcache.system.request
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.encodedPath

class AccountApiRest(
    private val httpProvider: HttpProvider
) : AccountApi {
    private fun buildUrl(id: String) = "/users/${id.lowercase()}.json"
    override suspend fun getAccountDetails(id: String): DataResult<AccountEntity> =
        httpProvider().request<AccountEntity> {
            method = HttpMethod.Get
            url {
                encodedPath = buildUrl(id)
            }
        }

    override suspend fun createAccount(id: String, details: AccountEntity): DataResult<Unit> =
        httpProvider().request<Unit> {
            method = HttpMethod.Put
            url {
                encodedPath = buildUrl(id)
            }
            setBody(details)
        }

}
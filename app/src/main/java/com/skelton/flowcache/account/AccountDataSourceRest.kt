package com.skelton.flowcache.account

import com.skelton.flowcache.DataResult
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.encodedPath

class AccountDataSourceRest(
    private val httpProvider: HttpProvider
) : AccountDataSource {

    fun buildUrl(name:String) = "https://flowcache-8615f-default-rtdb.europe-west1.firebasedatabase.app/users/${name.lowercase()}.json"
    override suspend fun getAccountDetails(name: String): DataResult<AccountDetails> =
        httpProvider().request<AccountDetails> {
            url {
                encodedPath = buildUrl(name)
            }
            method = HttpMethod.Get
        }

    override suspend fun createAccount(name: String, details: AccountDetails): DataResult<Unit> =
        httpProvider().request<Unit> {
            method = HttpMethod.Post
            url {
                encodedPath = buildUrl(name)
            }
            setBody(details)
        }

}
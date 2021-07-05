package com.skelton.flowcache.account

import com.google.firebase.firestore.ktx.toObject
import com.skelton.flowcache.AppConfig
import com.skelton.flowcache.DataResult
import com.skelton.flowcache.system.FirestoreCollectionProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

interface AccountDataSource {
    suspend fun getAccountDetails(name: String): DataResult<AccountDetails>
    suspend fun createAccount(name: String, details: AccountDetails): DataResult<Unit>
}

class DefaultAccountDataSource(
    private val collectionProvider: FirestoreCollectionProvider,
    private val config: AppConfig
) : AccountDataSource {

    override suspend fun getAccountDetails(name: String): DataResult<AccountDetails> {

        delay(config.simulatedNetworkDelay.toMillis())

        return try {
            collectionProvider.accounts.document(name).get().await()
                .toObject<AccountDetails>()?.let { DataResult.Success.Network(it) }
                ?: DataResult.Error("Account for [$name] not found", DataResult.Error.Code.NotFound)
        } catch (ex: Exception) {
            println(ex)
            DataResult.Error(ex.localizedMessage!!)
        }
    }

    override suspend fun createAccount(name: String, details: AccountDetails): DataResult<Unit> =
        try {
            collectionProvider.accounts.document(name).set(details).await()
            DataResult.Success.Network(Unit)
        } catch (ex: Exception) {
            println(ex)
            DataResult.Error(ex.localizedMessage!!)
        }
}

package com.skelton.flowcache

import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

interface AccountDataSource {
    suspend fun getAccountDetails(name: String): Result<AccountDetails>
    suspend fun createAccount(name: String, details: AccountDetails): Result<Unit>
}

class DefaultAccountDataSource(
    private val collectionProvider: FirestoreCollectionProvider,
    private val config: AppConfig
) : AccountDataSource {

    override suspend fun getAccountDetails(name: String): Result<AccountDetails> {

        delay(config.simulatedNetworkDelay.toMillis())

        return try {
            collectionProvider.accounts.document(name).get().await()
                .toObject<AccountDetails>()?.let { Result.Success.Network(it) }
                ?: Result.Error("Account for [$name] not found", Result.Error.Code.NotFound)
        } catch (ex: Exception) {
            println(ex)
            Result.Error(ex.localizedMessage)
        }
    }

    override suspend fun createAccount(name: String, details: AccountDetails): Result<Unit> =
        try {
            collectionProvider.accounts.document(name).set(details).await()
            Result.Success.Network(Unit)
        } catch (ex: Exception) {
            println(ex)
            Result.Error(ex.localizedMessage)
        }
}

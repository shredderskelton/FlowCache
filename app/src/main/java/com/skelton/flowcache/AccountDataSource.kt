package com.skelton.flowcache

import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

interface AccountDataSource {
    suspend fun getAccountDetails(): Result<AccountDetails>
}

class DefaultAccountDataSource(private val collectionProvider: FirestoreCollectionProvider) :
    AccountDataSource {
    override suspend fun getAccountDetails(): Result<AccountDetails> {

        delay(5000)
        return try {
            val account =
                collectionProvider.accounts.document("nick").get().await()
                    .toObject<AccountDetails>()!!

            println("Got a result")

            Result.Success.Network(
                // AccountDetails(
                //     "Nick Skelton",
                //     "nick.g.skelton@gmail.com",
                //     "Redhorn Pl 32, 43234 Munich"
                // )
                account
            )
        } catch (ex: Exception) {
            println("Error")
            Result.Error(ex.localizedMessage)
        }
    }
}

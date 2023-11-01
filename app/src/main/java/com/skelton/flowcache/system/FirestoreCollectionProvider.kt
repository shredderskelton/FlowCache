package com.skelton.flowcache.system

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.PersistentCacheSettings

interface FirestoreCollectionProvider {
    val accounts: CollectionReference
}

class DefaultFirestoreCollectionProvider : FirestoreCollectionProvider {

    private val db = FirebaseFirestore.getInstance()

    init {
        val settings = FirebaseFirestoreSettings.Builder()
            .setLocalCacheSettings(
                PersistentCacheSettings.newBuilder()
                    .setSizeBytes(0)
                    .build()
            )
            .build()
        db.firestoreSettings = settings
    }

    override val accounts = db.collection("accounts")
}
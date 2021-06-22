package com.skelton.flowcache

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

interface FirestoreCollectionProvider {
    val accounts: CollectionReference
}

class DefaultFirestoreCollectionProvider() : FirestoreCollectionProvider {

    private val db = FirebaseFirestore.getInstance()

    init {
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build();
        db.firestoreSettings = settings;
    }

    override val accounts = db.collection("accounts")
}
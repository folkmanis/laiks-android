package com.folkmanis.laiks.data.implementations

import com.folkmanis.laiks.LOCALES_COLLECTION
import com.folkmanis.laiks.data.LocalesService
import com.folkmanis.laiks.model.Locale
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LocalesServiceFirebase @Inject constructor(
    firestore: FirebaseFirestore,
) : LocalesService {

    private val collection = firestore.collection(LOCALES_COLLECTION)

    override suspend fun getLocales(): List<Locale> {
        return collection
            .orderBy("id")
            .get()
            .await()
            .toObjects()
    }

    override suspend fun getLocale(id: String): Locale? {
        return collection
            .document(id)
            .get()
            .await()
            .toObject()
    }

}
package com.folkmanis.laiks.data

import com.folkmanis.laiks.model.NpPrice
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PricesServiceFirebase(
    private val firestore: FirebaseFirestore
) : PricesService {

   private val npData = firestore
        .collection(LAIKS_COLLECTION)
        .document(NP_DATA)

    override val allNpPrices: Flow<List<NpPrice>>
        get() = npData.collection(NP_PRICES_COLLECTION)
            .orderBy("startTime")
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects()
            }

    companion object {
        private const val LAIKS_COLLECTION = "laiks"
        private const val NP_DATA = "np-data"
        private const val NP_PRICES_COLLECTION = "prices"
    }
}
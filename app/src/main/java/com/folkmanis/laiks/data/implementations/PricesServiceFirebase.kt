package com.folkmanis.laiks.data.implementations

import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.model.*
import com.folkmanis.laiks.utilities.ext.minusDays
import com.folkmanis.laiks.utilities.ext.toLocalDateTime
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import javax.inject.Inject

class PricesServiceFirebase @Inject constructor(
    private val firestore: FirebaseFirestore
) : PricesService {

    private val npPricesDocumentRef = firestore
        .collection(LAIKS_COLLECTION)
        .document(NP_DATA)

    private val npPricesCollection = npPricesDocumentRef
        .collection(NP_PRICES_COLLECTION)

    override suspend fun npPrices(startTimestamp: Timestamp): List<NpPrice> {
        return npPricesCollection
            .whereGreaterThanOrEqualTo("startTime", startTimestamp)
            .orderBy("startTime")
            .get()
            .await()
            .toObjects()
    }


    override fun lastDaysPricesFlow(days: Long): Flow<List<NpPrice>> {
        return npPricesCollection
            .orderBy("endTime", Query.Direction.DESCENDING)
            .limit(1)
            .snapshots()
            .map { snapshot ->
                snapshot
                    .toObjects<NpPrice>()
                    .first()
                    .endTime
                    .minusDays(days)
            }
            .map { fromTime ->
                npPrices(fromTime)
            }
    }

    override suspend fun lastUpdate(): LocalDateTime {
        val lastPrice = npPricesCollection
            .orderBy("startTime", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()
            .toObjects<NpPrice>()
            .firstOrNull()
        return lastPrice?.startTime?.toLocalDateTime() ?: LocalDateTime.MIN
    }

    override suspend fun uploadPrices(prices: List<NpPrice>) {
        val batch = firestore.batch()
        prices.forEach { price ->
            val docRef = npPricesCollection.document(price.startTime.toString())
            batch.set(docRef, price)
        }
        batch.set(
            npPricesDocumentRef,
            NpPricesDocument(),
            SetOptions.merge()
        )
       batch.commit().await()
    }


    companion object {
        @Suppress("unused")
        private const val TAG = "Prices Service"
        private const val LAIKS_COLLECTION = "laiks"
        private const val NP_DATA = "np-data"
        private const val NP_PRICES_COLLECTION = "prices"
    }
}
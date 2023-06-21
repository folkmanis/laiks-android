package com.folkmanis.laiks.data.implementations

import android.util.Log
import com.folkmanis.laiks.NP_DATA
import com.folkmanis.laiks.NP_PRICES_COLLECTION
import com.folkmanis.laiks.LAIKS_COLLECTION
import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.model.*
import com.folkmanis.laiks.utilities.ext.instant.toTimestamp
import com.folkmanis.laiks.utilities.ext.minusDays
import com.folkmanis.laiks.utilities.ext.toInstant
import com.folkmanis.laiks.utilities.ext.toLocalDateTime
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.time.Instant
import javax.inject.Inject


class PricesServiceFirebase @Inject constructor(
    private val firestore: FirebaseFirestore
) : PricesService {

    private val npPricesDocumentRef = firestore
        .collection(LAIKS_COLLECTION)
        .document(NP_DATA)

    private val npPricesCollection = npPricesDocumentRef
        .collection(NP_PRICES_COLLECTION)

    override suspend fun npPrices(start: Instant): List<NpPrice> {
        return npPricesCollection
            .whereGreaterThanOrEqualTo("startTime", start.toTimestamp())
            .orderBy("startTime")
            .get()
            .await()
            .toObjects()
    }


    override fun lastDaysPricesFlow(days: Long): Flow<List<NpPrice>> {
        return npPricesCollection
            .orderBy("startTime", Query.Direction.DESCENDING)
            .limit(1)
            .snapshots()
            .map {
                it.toObjects<NpPrice>().firstOrNull()?.endTime ?: Timestamp.now()
            }
            .map { it.minusDays(days) }
            .map { fromTime ->
                Log.d(TAG, "Prices from day ${fromTime.toLocalDateTime()}")
                npPrices(fromTime.toInstant())
            }
    }

    override fun pricesFromDateTimeFlow(dateTime: Instant): Flow<List<NpPrice>> =
        npPricesCollection
            .orderBy("startTime")
            .whereGreaterThanOrEqualTo(
                "startTime",
                dateTime.toTimestamp()
            )
            .snapshots()
            .map { it.toObjects() }

    override suspend fun latestPriceStartTime(): Instant {
        val lastPrice = npPricesCollection
            .orderBy("startTime", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()
            .toObjects<NpPrice>()
            .firstOrNull()
        return lastPrice?.startTime?.toInstant() ?: Instant.MIN
    }

    override suspend fun uploadPrices(
        prices: List<NpPrice>,
        npPricesDocument: NpPricesDocument
    ) {
        val batch = firestore.batch()
        prices.forEach { price ->
            val docRef = npPricesCollection.document(price.id)
            batch.set(docRef, price)
        }
        batch.set(
            npPricesDocumentRef,
            npPricesDocument,
            SetOptions.merge()
        )
        batch.commit().await()
    }

    override suspend fun npPricesDocument(): NpPricesDocument? = npPricesDocumentRef
            .get()
            .await()
            .toObject()

    override fun npPricesDocumentFlow(): Flow<NpPricesDocument?> =
        npPricesDocumentRef
            .snapshots()
            .map { it.toObject() }


    companion object {
        @Suppress("unused")
        private const val TAG = "Prices Service"
    }
}
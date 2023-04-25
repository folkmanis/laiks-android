package com.folkmanis.laiks.data.implementations

import android.util.Log
import com.folkmanis.laiks.BuildConfig
import com.folkmanis.laiks.NP_DATA
import com.folkmanis.laiks.NP_PRICES_COLLECTION
import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.model.*
import com.folkmanis.laiks.utilities.ext.minusDays
import com.folkmanis.laiks.utilities.ext.toInstant
import com.folkmanis.laiks.utilities.ext.toLocalDateTime
import com.folkmanis.laiks.utilities.ext.toTimestamp
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
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject


class PricesServiceFirebase @Inject constructor(
    private val firestore: FirebaseFirestore
) : PricesService {

    private val npPricesDocumentRef = firestore
        .collection(BuildConfig.LAIKS_COLLECTION)
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
            .orderBy("startTime", Query.Direction.DESCENDING)
            .limit(1)
            .snapshots()
            .map {
                it.toObjects<NpPrice>().firstOrNull()?.endTime ?: Timestamp.now()
            }
            .map { it.minusDays(days) }
            .map { fromTime ->
                Log.d(TAG, "Prices from day ${fromTime.toLocalDateTime()}")
                npPrices(fromTime)
            }
    }

    override fun pricesFromDateTime(dateTime: LocalDateTime): Flow<List<NpPrice>> =
        npPricesCollection
            .orderBy("startTime")
            .whereGreaterThanOrEqualTo(
                "startTime",
                dateTime.atZone(ZoneId.systemDefault()).toTimestamp()
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

    override suspend fun uploadPrices(prices: List<NpPrice>) {
        val batch = firestore.batch()
        prices.forEach { price ->
            val docRef = npPricesCollection.document(price.id)
            batch.set(docRef, price)
        }
        batch.set(
            npPricesDocumentRef,
            NpPricesDocument(),
            SetOptions.merge()
        )
        batch.commit().await()
    }

    override suspend fun lastUpdate(): Instant {
        val doc: NpPricesDocument? = npPricesDocumentRef
            .get()
            .await()
            .toObject()
        return doc?.lastUpdate?.toInstant() ?: Instant.MIN
    }


    companion object {
        @Suppress("unused")
        private const val TAG = "Prices Service"
    }
}
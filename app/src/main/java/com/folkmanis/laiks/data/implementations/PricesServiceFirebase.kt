package com.folkmanis.laiks.data.implementations

import android.util.Log
import com.folkmanis.laiks.LAIKS_COLLECTION
import com.folkmanis.laiks.NP_PRICES_COLLECTION
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.MarketZonesService
import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.NpPricesDocument
import com.folkmanis.laiks.utilities.MarketZoneNotSetException
import com.folkmanis.laiks.utilities.ext.instant.toTimestamp
import com.folkmanis.laiks.utilities.ext.minusDays
import com.folkmanis.laiks.utilities.ext.toInstant
import com.folkmanis.laiks.utilities.ext.toLocalDateTime
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.time.Instant
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
class PricesServiceFirebase @Inject constructor(
    firestore: FirebaseFirestore,
    private val laiksUserService: LaiksUserService,
    private val zonesService: MarketZonesService,
) : PricesService {

    private val collection = firestore
        .collection(LAIKS_COLLECTION)

    private suspend fun npPricesDocumentRef(): DocumentReference? {
        return laiksUserService.laiksUser()?.marketZoneId?.let { zoneId ->
            zonesService.getMarketZone(zoneId)?.dbName
        }?.let { dbName ->
            collection.document(dbName)
        }
    }

    private fun npPricesDocumentRefFlow(): Flow<DocumentReference> =
        laiksUserService.laiksUserFlow()
            .filterNotNull()
            .map { it.marketZoneId ?: throw MarketZoneNotSetException() }
            .map { zonesService.getMarketZone(it) }
            .filterNotNull()
            .map { collection.document(it.dbName) }

    private fun npPricesCollectionRefFlow(): Flow<CollectionReference> =
        npPricesDocumentRefFlow()
            .map { it.collection(NP_PRICES_COLLECTION) }

    private suspend fun npPricesCollection() = npPricesDocumentRef()
        ?.collection(NP_PRICES_COLLECTION)

    override suspend fun npPrices(start: Instant): List<NpPrice> {
        return npPricesCollection()
            ?.whereGreaterThanOrEqualTo("startTime", start.toTimestamp())
            ?.orderBy("startTime")
            ?.get()
            ?.await()
            ?.toObjects() ?: emptyList()
    }


    override fun lastDaysPricesFlow(days: Long): Flow<List<NpPrice>> {
        return npPricesCollectionRefFlow()
            .flatMapLatest { collectionRef ->
                collectionRef
                    .orderBy("startTime", Query.Direction.DESCENDING)
                    .limit(1)
                    .snapshots()
            }
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
        npPricesCollectionRefFlow()
            .flatMapLatest { collectionRef ->
                collectionRef
                    .orderBy("startTime")
                    .whereGreaterThanOrEqualTo(
                        "startTime",
                        dateTime.toTimestamp()
                    )
                    .snapshots()
            }
            .map { it.toObjects() }

    override suspend fun latestPriceStartTime(): Instant {
        val lastPrice = npPricesCollection()
            ?.orderBy("startTime", Query.Direction.DESCENDING)
            ?.limit(1)
            ?.get()
            ?.await()
            ?.toObjects<NpPrice>()
            ?.firstOrNull()
        return lastPrice?.startTime?.toInstant() ?: Instant.MIN
    }

    override suspend fun npPricesDocument(): NpPricesDocument? = npPricesDocumentRef()
        ?.get()
        ?.await()
        ?.toObject()

    override fun npPricesDocumentFlow(): Flow<NpPricesDocument?> =
        npPricesDocumentRefFlow()
            .flatMapLatest { it.snapshots() }
            .map { it.toObject() }


    companion object {
        @Suppress("unused")
        private const val TAG = "Prices Service"
    }
}
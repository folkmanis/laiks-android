package com.folkmanis.laiks.data.implementations

import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.model.*
import com.folkmanis.laiks.utilities.ext.minusDays
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PricesServiceFirebase @Inject constructor(
    private val firestore: FirebaseFirestore
) : PricesService {

    private val npData = firestore
        .collection(LAIKS_COLLECTION)
        .document(NP_DATA)

    override suspend fun npPrices(startTimestamp: Timestamp): List<NpPrice> {
        return npData.collection(NP_PRICES_COLLECTION)
            .whereGreaterThanOrEqualTo("startTime", startTimestamp)
            .orderBy("startTime")
            .get()
            .await()
            .toObjects()
    }

    override suspend fun activeAppliances(): List<PowerAppliance> {
        return firestore.collection(POWER_APPLIANCES_COLLECTION)
            .whereEqualTo("enabled", true)
            .orderBy("name")
            .get()
            .await()
            .toObjects()
    }

    override fun lastDaysPricesFlow(days: Long): Flow<List<NpPrice>> {
        return firestore
            .collection(LAIKS_COLLECTION)
            .document(NP_DATA)
            .collection(NP_PRICES_COLLECTION)
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


    companion object {
        @Suppress("unused")
        private const val TAG = "Prices Service"
        private const val LAIKS_COLLECTION = "laiks"
        private const val NP_DATA = "np-data"
        private const val NP_PRICES_COLLECTION = "prices"
        private const val POWER_APPLIANCES_COLLECTION = "powerAppliances"
    }
}
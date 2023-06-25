package com.folkmanis.laiks.data.implementations

import com.folkmanis.laiks.MARKET_ZONES_COLLECTION
import com.folkmanis.laiks.data.MarketZonesService
import com.folkmanis.laiks.model.MarketZone
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.google.firebase.firestore.ktx.toObjects


class MarketZonesServiceFirebase @Inject constructor(
    firestore: FirebaseFirestore,
) : MarketZonesService {

    private val collection = firestore.collection(MARKET_ZONES_COLLECTION)

    override suspend fun getMarketZones(): List<MarketZone> {
        return collection
            .orderBy("id")
            .get()
            .await()
            .toObjects()
    }

    override suspend fun getMarketZone(id: String): MarketZone? {
        return collection
            .document(id)
            .get()
            .await()
            .toObject()
    }

}
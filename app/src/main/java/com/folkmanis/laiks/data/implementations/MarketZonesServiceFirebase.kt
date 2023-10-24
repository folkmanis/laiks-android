package com.folkmanis.laiks.data.implementations

import com.folkmanis.laiks.MARKET_ZONES_COLLECTION
import com.folkmanis.laiks.data.MarketZonesService
import com.folkmanis.laiks.model.MarketZone
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class MarketZonesServiceFirebase @Inject constructor(
    firestore: FirebaseFirestore,
) : MarketZonesService {

    private val collection = firestore.collection(MARKET_ZONES_COLLECTION)

    override val marketZonesFlow: Flow<List<MarketZone>>
        get() = collection
            .snapshots()
            .map { it.toObjects() }
    override suspend fun getMarketZones(): List<MarketZone> {
        return collection
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
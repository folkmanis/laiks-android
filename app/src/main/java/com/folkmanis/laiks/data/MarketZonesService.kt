package com.folkmanis.laiks.data

import com.folkmanis.laiks.model.MarketZone
import kotlinx.coroutines.flow.Flow

interface MarketZonesService {

    val marketZonesFlow: Flow<List<MarketZone>>

    suspend fun getMarketZones():List<MarketZone>

    suspend fun getMarketZone(id: String): MarketZone?

}
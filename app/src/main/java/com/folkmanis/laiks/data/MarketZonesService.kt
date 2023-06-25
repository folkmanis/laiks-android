package com.folkmanis.laiks.data

import com.folkmanis.laiks.model.MarketZone

interface MarketZonesService {

    suspend fun getMarketZones():List< MarketZone>

    suspend fun getMarketZone(id: String): MarketZone?

}
package com.folkmanis.laiks.data.fake

import com.folkmanis.laiks.data.MarketZonesService
import com.folkmanis.laiks.model.MarketZone
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeMarketZonesService : MarketZonesService {

    override suspend fun getMarketZones(): List<MarketZone> {
        return zonesList
    }

    override val marketZonesFlow: Flow<List<MarketZone>>
        get() = flowOf(zonesList)

    override suspend fun getMarketZone(id: String): MarketZone? {
        return zonesList.find { zone -> zone.id == id }
    }

    companion object {
        val zoneLV = MarketZone(
            id = "LV",
            description = "Latvija",
            locale = "lv",
            dbName = "np-data",
            url = "https://www.nordpoolgroup.com/api/marketdata/page/59?currency=,EUR,EUR,EUR",
            tax = 0.21,
        )
        val zoneSE1 = MarketZone(
            id = "SE1",
            description = "Luleå",
            locale = "se",
            dbName = "SE1",
            url = "https://www.nordpoolgroup.com/api/marketdata/page/29?currency=,EUR,EUR,EUR&entityName=SE1",
            tax = 0.25,
        )

        val zonesList = listOf(
            zoneLV,
            zoneSE1,
        )
    }
}
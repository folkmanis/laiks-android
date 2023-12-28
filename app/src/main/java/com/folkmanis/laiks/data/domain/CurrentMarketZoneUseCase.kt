package com.folkmanis.laiks.data.domain

import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.MarketZonesService
import com.folkmanis.laiks.model.MarketZone
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CurrentMarketZoneUseCase @Inject constructor(
    private val laiksUserService: LaiksUserService,
    private val marketZonesService: MarketZonesService,
) {

    operator fun invoke(): Flow<MarketZone> = laiksUserService.laiksUserFlow()
        .map { it?.marketZoneId }
        .filterNotNull()
        .map { marketZoneId ->
            marketZonesService.getMarketZone(marketZoneId)
        }
        .filterNotNull()
}
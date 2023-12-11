package com.folkmanis.laiks.ui.screens.market_zone_select

import com.folkmanis.laiks.model.MarketZone

data class MarketZoneSelectUiState(
    val zoneId: String? = null,
    val marketZones: List<MarketZone>? = null,
    val vatAmount: Long? = null,
    val vatEnabled: Boolean = true,
    val busy: Boolean = false,
) {
    fun isValid(): Boolean =
        zoneId != null
                && vatAmount != null
}

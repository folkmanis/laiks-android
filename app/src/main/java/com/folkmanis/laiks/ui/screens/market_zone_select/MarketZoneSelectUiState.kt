package com.folkmanis.laiks.ui.screens.market_zone_select

import com.folkmanis.laiks.model.MarketZone
import com.folkmanis.laiks.utilities.ext.isValidVat

data class MarketZoneSelectUiState(
    val zoneId: String? = null,
    val marketZones: List<MarketZone>? = null,
    val vatAmount: Double? = null,
    val vatEnabled: Boolean = true,
    val busy: Boolean = false,
) {
    fun isValid(): Boolean =
        zoneId != null
                && vatAmount != null
                && vatAmount.isValidVat()
}

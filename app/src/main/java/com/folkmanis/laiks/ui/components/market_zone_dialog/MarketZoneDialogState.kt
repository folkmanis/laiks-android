package com.folkmanis.laiks.ui.components.market_zone_dialog

import com.folkmanis.laiks.model.MarketZone

data class MarketZoneDialogState(
    val zones: List<MarketZone> = emptyList(),
    val currentZoneId: String? = null,
    val initialZoneId: String? =null,
    val busy: Boolean = false,
) {
    val currentZone
        get(): MarketZone? = zones.find { it.id == currentZoneId }

}
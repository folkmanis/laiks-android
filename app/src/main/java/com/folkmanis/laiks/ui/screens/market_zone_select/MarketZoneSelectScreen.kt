package com.folkmanis.laiks.ui.screens.market_zone_select

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.folkmanis.laiks.model.MarketZone
import com.folkmanis.laiks.utilities.composables.LazyItemSelection
import com.folkmanis.laiks.utilities.composables.LoadingScreen

@Composable
fun MarketZoneSelectScreen(
    modifier: Modifier = Modifier,
    uiState: MarketZoneSelectUiState,
    onMarketZoneSelected: (String) -> Unit,
) {

    Column {



        MarketZonesList(
            marketZones = uiState.marketZones,
            onMarketZoneSelected = onMarketZoneSelected,
            selectedZoneId = uiState.zoneId
        )
    }


}

@Composable
fun MarketZonesList(
    modifier: Modifier = Modifier,
    marketZones: List<MarketZone>?,
    onMarketZoneSelected: (String) -> Unit,
    selectedZoneId: String?,
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (marketZones != null)
            LazyItemSelection(
                data = marketZones,
                onItemSelected = onMarketZoneSelected,
                selectedId = selectedZoneId,
            )
        else
            LoadingScreen()
    }
}

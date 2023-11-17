package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.MarketZonesService
import com.folkmanis.laiks.model.MarketZone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MarketZoneInputViewModel @Inject constructor(
    zonesService: MarketZonesService,
) : ViewModel() {

    val uiState: StateFlow<MarketZonesState> =
        zonesService.marketZonesFlow
            .map { zones ->
                MarketZonesState(loading = false, zones = zones)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MarketZonesState(),
            )

}

data class MarketZonesState(
    val loading: Boolean = true,
    val zones: List<MarketZone> = emptyList(),
)
package com.folkmanis.laiks.ui.screens.user_settings

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
            .map(MarketZonesState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MarketZonesState.Loading,
            )

}

sealed interface MarketZonesState {
    object Loading : MarketZonesState
    data class Success(
        val zones: List<MarketZone>
    ) : MarketZonesState
}
package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import androidx.lifecycle.ViewModel
import com.folkmanis.laiks.data.MarketZonesService
import com.folkmanis.laiks.model.MarketZone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MarketZoneInputViewModel @Inject constructor(
    private val zonesService: MarketZonesService,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MarketZonesState>(MarketZonesState.Loading)
    val uiState = _uiState.asStateFlow()

    suspend fun initialize(initialZoneId: String?) {
        val zones = zonesService.getMarketZones()
            .filter { zone -> zone.enabled }

        _uiState.value = MarketZonesState.Success(
            zones = zones,
            currentZoneId = initialZoneId,
            initialZoneId = initialZoneId,
        )
    }

    fun setZoneId(newZoneId: String) {
        _uiState.update { state ->
            if (state is MarketZonesState.Success) {
                state.copy(currentZoneId = newZoneId)
            } else state
        }
    }

}


sealed interface MarketZonesState {
    data class Success(
        val zones: List<MarketZone> = emptyList(),
        val currentZoneId: String?,
        val initialZoneId: String?,
    ) : MarketZonesState {
        val saveEnabled: Boolean
            get() = currentZoneId != null
                    && currentZoneId != initialZoneId

        fun getCurrentZone(): MarketZone? = zones.find { it.id == currentZoneId }
    }

    data object Loading : MarketZonesState

}
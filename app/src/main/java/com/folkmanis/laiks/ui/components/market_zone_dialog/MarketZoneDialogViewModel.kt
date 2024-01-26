package com.folkmanis.laiks.ui.components.market_zone_dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.MarketZonesService
import com.folkmanis.laiks.model.MarketZone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketZoneDialogViewModel @Inject constructor(
    private val zonesService: MarketZonesService,
    private val laiksUserService: LaiksUserService,
) : ViewModel() {

    var uiState by mutableStateOf(MarketZoneDialogState())
        private set

    private var busy: Boolean
        set(value) {
            uiState = uiState.copy(busy = value)
        }
        get() = uiState.busy

    suspend fun initialize() {

        uiState = uiState.copy(
            zones = zonesService.getMarketZones()
                .filter { zone -> zone.enabled }
        )

        laiksUserService.laiksUser()?.also { laiksUser ->
            uiState = uiState.copy(
                currentZoneId = laiksUser.marketZoneId,
                initialZoneId = laiksUser.marketZoneId,
            )
        }
    }

    fun setZoneId(
        newZoneId: String,
        onSaved: (MarketZone) -> Unit
    ) {

        uiState = uiState.copy(currentZoneId = newZoneId)

        busy = true
        uiState.currentZone?.also { zone ->
            viewModelScope.launch {
                laiksUserService.updateLaiksUser(
                    hashMapOf(
                        "marketZoneId" to zone.id,
                        "vatAmount" to zone.tax
                    )
                )
                busy = false
                onSaved(zone)
            }
        }
    }

}
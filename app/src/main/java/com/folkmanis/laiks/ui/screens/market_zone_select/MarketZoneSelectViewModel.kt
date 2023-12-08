package com.folkmanis.laiks.ui.screens.market_zone_select

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.MarketZonesService
import com.folkmanis.laiks.model.LaiksUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketZoneSelectViewModel @Inject constructor(
    private val marketZonesService: MarketZonesService,
    private val laiksUserService: LaiksUserService,
) : ViewModel() {

    var uiState by mutableStateOf(MarketZoneSelectUiState())
        private set

    private var busy
        get() = uiState.busy
        set(value) {
            uiState = uiState.copy(busy = value)
        }

    fun initialize(laiksUser: LaiksUser) {

        uiState = uiState.copy(
            zoneId = laiksUser.marketZoneId,
            vatAmount = laiksUser.vatAmount,
            vatEnabled = laiksUser.includeVat,
        )

        viewModelScope.launch {
            val marketZones = marketZonesService.getMarketZones()
            uiState = uiState.copy(marketZones = marketZones)
        }

    }

    fun setMarketZoneId(newZoneId: String) {
        uiState = uiState.copy(zoneId = newZoneId)
        uiState.marketZones
            ?.find { zone -> zone.id == newZoneId }
            ?.also { zone -> setVatAmount(zone.tax) }
    }

    fun setVatAmount(newVat: Double) {
        uiState = uiState.copy(vatAmount = newVat)
    }

    fun setVatEnabled(isEnabled: Boolean) {
        uiState = uiState.copy(vatEnabled = isEnabled)
    }

    fun saveSettings(onSuccess: () -> Unit) {

        if (!uiState.isValid()) return

        busy = true
        val update = hashMapOf<String, Any>(
            "marketZoneId" to uiState.zoneId!!,
            "includeVat" to uiState.vatEnabled,
            "vatAmount" to uiState.vatAmount!!,
        )

        viewModelScope.launch {
            laiksUserService.updateLaiksUser(update)
            busy = false
        }

    }

}
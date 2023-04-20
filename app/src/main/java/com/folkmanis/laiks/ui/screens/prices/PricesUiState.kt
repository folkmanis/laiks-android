package com.folkmanis.laiks.ui.screens.prices

import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.model.PowerHour
import java.time.LocalDate
import java.time.LocalDateTime

sealed interface PricesUiState {
    object Loading : PricesUiState
    data class Error(
        val reason: String,
        val exception: Throwable,
        ) : PricesUiState
    data class Success(
        val npPrices: List<NpPrice> = emptyList(),
        val appliances: List<PowerAppliance> = emptyList(),
        val minute: LocalDateTime = LocalDateTime.now(),
        val groupedCosts: Map<LocalDate, List<PowerHour>> = emptyMap(),
    ) : PricesUiState
}

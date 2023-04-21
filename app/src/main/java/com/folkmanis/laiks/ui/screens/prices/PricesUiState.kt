package com.folkmanis.laiks.ui.screens.prices

import com.folkmanis.laiks.model.NpPrice
import java.time.LocalDate
import java.time.LocalDateTime

sealed interface PricesUiState {
    object Loading : PricesUiState
    data class Error(
        val reason: String,
        val exception: Throwable,
        ) : PricesUiState
    data class Success(
//        val npPrices: List<NpPrice> = emptyList(),
//        val appliances: List<PowerAppliance> = emptyList(),
        val groupedPrices: Map<LocalDate, List<NpPrice>> = emptyMap(),
        val hour: LocalDateTime = LocalDateTime.now(),
    ) : PricesUiState

    companion object {
        fun List<NpPrice>.toPowerHours() {

        }
    }
}

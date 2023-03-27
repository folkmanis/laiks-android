package com.folkmanis.laiks.ui.screens.prices

import com.folkmanis.laiks.model.PowerHour
import java.time.LocalDate

sealed interface PricesUiState {
    object Loading : PricesUiState
    data class Error(
        val reason: String,
        val exception: Throwable,
        ) : PricesUiState
    data class Success(
        val groupedPrices: Map<LocalDate, List<PowerHour>>
    ) : PricesUiState
}

package com.folkmanis.laiks.ui.screens.prices

import com.folkmanis.laiks.model.NpPrice
import java.time.LocalDate
import java.time.LocalDateTime

sealed interface PricesUiState {
    object Loading : PricesUiState
    data class Error(
        val reason: String?,
        val exception: Throwable,
    ) : PricesUiState

    data class Success(
        val groupedPrices: Map<LocalDate, List<NpPrice>> = emptyMap(),
        val hour: LocalDateTime = LocalDateTime.now(),
        val currentOffsetIndex: Int = 0,
    ) : PricesUiState

}

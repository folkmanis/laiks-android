package com.folkmanis.laiks.ui.screens.prices

import androidx.compose.runtime.Stable
import com.folkmanis.laiks.model.NpPrice
import java.time.LocalDate
import java.time.LocalDateTime

@Stable
sealed interface PricesUiState {
    data object Loading : PricesUiState
    data class Error(
        val reason: String?,
        val exception: Throwable,
    ) : PricesUiState

    data class Success(
        val groupedPrices: Map<LocalDate, List<NpPrice>> = emptyMap(),
        val hour: LocalDateTime = LocalDateTime.now(),
        val currentOffsetIndex: Int = 0,
    ) : PricesUiState

    data object MarketZoneMissing : PricesUiState

}

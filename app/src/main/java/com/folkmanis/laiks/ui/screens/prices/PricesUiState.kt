package com.folkmanis.laiks.ui.screens.prices

import androidx.compose.runtime.Stable
import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.utilities.ext.hoursFrom
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
        val listItemsData: List<PricesListItemData> = emptyList(),
        val hour: LocalDateTime = LocalDateTime.now(),
        val currentOffsetIndex: Int = 0,
    ) : PricesUiState

    data object MarketZoneMissing : PricesUiState

}


sealed interface PricesListItemData {
    data class DateHeader(val date: LocalDate) : PricesListItemData
    data class HourlyPrice(val npPrices: List<NpPrice>) : PricesListItemData
}
package com.folkmanis.laiks.ui.screens.prices

import com.folkmanis.laiks.model.NpPrice
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

sealed interface PricesUiState {
    object Loading : PricesUiState
    data class Error(val reason: String) : PricesUiState
    data class Success(
        val localDateTime: LocalDateTime = LocalDateTime.now(),
        val npPrices: List<NpPrice> = emptyList()
    ) : PricesUiState {
        val instantTime: Instant
            get() = localDateTime
                .atZone(ZoneId.systemDefault())
                .toInstant()
    }
}
package com.folkmanis.laiks.ui.screens.prices

import com.folkmanis.laiks.model.NpPrice
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

sealed interface PricesUiState {
    object Loading : PricesUiState
    data class Error(
        val reason: String,
        val exception: Throwable,
        ) : PricesUiState
    data class Success(
        val hour: LocalDateTime = LocalDateTime.now(),
        val npPrices: List<NpPrice> = emptyList(),
        val minute: LocalDateTime = LocalDateTime.now(),
    ) : PricesUiState {
        val instantHour: Instant
            get() = hour
                .atZone(ZoneId.systemDefault())
                .toInstant()
    }
}
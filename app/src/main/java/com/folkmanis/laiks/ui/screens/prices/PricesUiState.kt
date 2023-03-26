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

/*

        val hour: LocalDateTime = LocalDateTime.now(),
        val npPrices: List<NpPrice> = emptyList(),
        val minute: LocalDateTime = LocalDateTime.now(),
        val powerApplianceCostsList: List< PowerApplianceCosts> = emptyList(),

{
        val instantHour: Instant
            get() = hour
                .atZone(ZoneId.systemDefault())
                .toInstant()
    }
*/
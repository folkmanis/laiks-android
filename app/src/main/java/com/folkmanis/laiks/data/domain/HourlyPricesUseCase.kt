package com.folkmanis.laiks.data.domain

import com.folkmanis.laiks.VAT
import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.data.UserPreferencesRepository
import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.utilities.ext.addVat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class HourlyPricesUseCase @Inject constructor(
    private val pricesRepository: PricesService,
    userPreferencesRepository: UserPreferencesRepository,
) {

    private val vatAmount = userPreferencesRepository.includeVat
        .map { if (it) VAT else 1.0 }

    operator fun invoke(dateTime: LocalDateTime): Flow<List<NpPrice>> {

        val instant = dateTime
            .truncatedTo(ChronoUnit.HOURS)
            .atZone(ZoneId.systemDefault())
            .toInstant()

        return pricesRepository.pricesFromDateTimeFlow(instant)
            .combine(vatAmount) { prices, vat ->
                prices.addVat(vat)
            }
    }
}
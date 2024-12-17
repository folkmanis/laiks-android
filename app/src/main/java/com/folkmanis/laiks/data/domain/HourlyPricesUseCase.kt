package com.folkmanis.laiks.data.domain

import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.utilities.ext.addExtraCost
import com.folkmanis.laiks.utilities.ext.addVat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class HourlyPricesUseCase @Inject constructor(
    private val pricesRepository: PricesService,
    private val laiksUserService: LaiksUserService,
) {

    operator fun invoke(hour: LocalDateTime): Flow<List<NpPrice>> {
        val instant = hour.truncatedTo(ChronoUnit.HOURS)
            .atZone(ZoneId.systemDefault())
            .toInstant()

        return pricesRepository.pricesFromDateTimeFlow(instant)
            .combine(laiksUserService.priceExtrasFlow) { prices, extra ->
                prices.addExtraCost(extra)
            }
            .combine(laiksUserService.vatAmountFlow) { prices, vat ->
                prices.addVat(vat)
            }
    }
}
package com.folkmanis.laiks.data.domain

import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.model.ApplianceHourWithCosts
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.utilities.cycleLengthSeconds
import com.folkmanis.laiks.utilities.ext.addVat
import com.folkmanis.laiks.utilities.hourTicks
import com.folkmanis.laiks.utilities.minuteTicks
import com.folkmanis.laiks.utilities.offsetCosts
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

private fun PowerAppliance.startTime(time: LocalDateTime): LocalDateTime =
    if (delay == "end") {
        time - Duration.ofSeconds(cycleLengthSeconds)
    } else {
        time
    }

private fun PowerAppliance.endTime(time: LocalDateTime): LocalDateTime =
    if (delay == "end") {
        time
    } else {
        time + Duration.ofSeconds(cycleLengthSeconds)
    }


@OptIn(ExperimentalCoroutinesApi::class)
class ApplianceHourlyCostsUseCase @Inject constructor(
    private val pricesService: PricesService,
    private val laiksUserService: LaiksUserService,
) {

    operator fun invoke(
        appliance: PowerAppliance,
    ): Flow<List<ApplianceHourWithCosts>> {

        val pricesFlow = hourTicks()
            .map { it.atZone(ZoneId.systemDefault()).toInstant() }
            .flatMapLatest { instant ->
                pricesService.pricesFromDateTimeFlow(instant)
            }
            .combine(laiksUserService.vatAmountFlow) { prices, vat ->
                prices.addVat(vat)
            }

        return combine(pricesFlow, minuteTicks()) { prices, startMinute ->
            val startInstant = startMinute.atZone(ZoneId.systemDefault()).toInstant()
            offsetCosts(prices, startInstant, appliance)
                .toList()
                .sortedBy { (_, value) -> value }
                .map { (offset, value) ->
                    val offsetMinute = startMinute.plusHours(offset)
                    ApplianceHourWithCosts(
                        offset = offset.toInt(),
                        value = value,
                        startTime = appliance.startTime(offsetMinute),
                        endTime = appliance.endTime(offsetMinute)
                    )
                }
        }
    }
}
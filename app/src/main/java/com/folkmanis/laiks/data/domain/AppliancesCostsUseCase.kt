package com.folkmanis.laiks.data.domain

import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.model.PowerApplianceHour
import com.folkmanis.laiks.utilities.bestOffset
import com.folkmanis.laiks.utilities.ext.hoursFrom
import com.folkmanis.laiks.utilities.offsetCosts
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

private fun List<PowerAppliance>.toPowerApplianceHour(
    appliancesAllCosts: Map<PowerAppliance, Map<Long, Double>>,
    offset: Int,
    bestOffsets: Map<PowerAppliance, Int>
): List<PowerApplianceHour> {
    val appliances = this
    return buildList {
        appliances.forEach { appliance ->
            val applianceHourCost = appliancesAllCosts[appliance]
                ?.get(offset.toLong())
            if (applianceHourCost != null) {
                add(
                    PowerApplianceHour(
                        name = appliance.name,
                        cost = applianceHourCost,
                        color = appliance.color,
                        isBest = bestOffsets[appliance] == offset,
                    )
                )
            }
        }
    }
}

class AppliancesCostsUseCase @Inject constructor(
    private val laiksUserService: LaiksUserService,
) {

    suspend operator fun invoke(
        prices: List<NpPrice>,
        minute: LocalDateTime,
    ): Map<Int, List<PowerApplianceHour>> {

        val appliances = laiksUserService.laiksUser().appliances

        val startTime = minute.atZone(ZoneId.systemDefault()).toInstant()
        val appliancesAllCosts: Map<PowerAppliance, Map<Long, Double>> =
            buildMap {
                appliances.forEach { appliance ->
                    val costs = offsetCosts(prices, startTime, appliance)
                    put(appliance, costs)
                }
            }

        val bestOffsets = buildMap {
            appliancesAllCosts.forEach { (powerAppliance, costs) ->
                val bestOffset = costs
                    .bestOffset()
                if (bestOffset != null) {
                    put(powerAppliance, bestOffset)
                }
            }
        }

        val hourlyCosts: Map<Int, List<PowerApplianceHour>> = buildMap {
            prices.forEach { npPrice ->
                val offset = npPrice.startTime.hoursFrom(minute)
                val appliancesHour = appliances.toPowerApplianceHour(
                    appliancesAllCosts,
                    offset,
                    bestOffsets
                )
                put(offset, appliancesHour)
            }
        }

        return hourlyCosts

    }

}
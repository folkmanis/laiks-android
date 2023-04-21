package com.folkmanis.laiks.ui.screens.prices

import com.folkmanis.laiks.model.PowerApplianceHour

data class AppliancesState(
    val powerAppliancesHours: Map<Int, List<PowerApplianceHour>> = emptyMap()
)

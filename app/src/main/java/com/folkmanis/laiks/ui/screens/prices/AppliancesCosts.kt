package com.folkmanis.laiks.ui.screens.prices

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.folkmanis.laiks.model.PowerApplianceHour
import com.folkmanis.laiks.utilities.ext.toFormattedDecimals

@Composable
fun AppliancesCosts(
    appliances: List<PowerApplianceHour>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        appliances.forEach { appliance ->
            ApplianceCost(appliance = appliance)
        }
    }
}

@Composable
fun ApplianceCost(
    appliance: PowerApplianceHour,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {

        Text(text = appliance.name)

        Text(text = ": ")

        Text(text = appliance.cost.toFormattedDecimals())

    }
}
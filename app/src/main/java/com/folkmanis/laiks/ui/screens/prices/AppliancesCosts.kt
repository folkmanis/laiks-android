package com.folkmanis.laiks.ui.screens.prices

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.folkmanis.laiks.model.PowerApplianceHour
import com.folkmanis.laiks.ui.theme.LaiksTheme
import com.folkmanis.laiks.utilities.ext.eurToCents
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
    Box(
        modifier = modifier
            .padding(1.dp)
            .height(18.dp)
    ) {

        val costStr = appliance.cost
            .eurToCents()
            .toFormattedDecimals()
        val text = "${appliance.name}: $costStr"
        val color = if (appliance.isBest) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurface
        }

        val backgroundColor = if(appliance.isBest) {
            Color(appliance.color.toColorInt())
        } else {
            MaterialTheme.colorScheme.surface
        }

        Text(
            text = text,
            fontSize = 10.sp,
            modifier = Modifier
                .background(backgroundColor, shape = RoundedCornerShape(9.dp))
                .padding(vertical = 1.dp, horizontal = 4.dp),
            color = color,
        )

    }
}

@Preview
@Composable
fun AppliancesCostsPreview() {
    val costs = listOf(
        PowerApplianceHour(isBest = true, cost = 0.185, name = "Veļasmašīna"),
        PowerApplianceHour(),
    )
    LaiksTheme {
        AppliancesCosts(appliances = costs)
    }
}
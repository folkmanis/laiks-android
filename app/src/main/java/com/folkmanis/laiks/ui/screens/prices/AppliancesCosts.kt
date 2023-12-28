package com.folkmanis.laiks.ui.screens.prices

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.folkmanis.laiks.model.PowerApplianceHour
import com.folkmanis.laiks.ui.theme.LaiksTheme
import com.folkmanis.laiks.utilities.ext.isDark
import com.folkmanis.laiks.utilities.ext.toFormattedDecimals

fun Color.contrasting(): Color =
    if (isDark()) Color.White else Color.Black

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

    val backgroundColor = if (appliance.isBest) {
        Color(appliance.color.toColorInt())
    } else {
        MaterialTheme.colorScheme.surface
    }
    val color = if (appliance.isBest) {
        backgroundColor.contrasting()
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val style = TextStyle(fontSize = 10.sp, lineHeight = 10.sp, color = color)

    Row(
        modifier = modifier
            .padding(1.dp)
            .height(18.dp)
            .width(IntrinsicSize.Min)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(9.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = appliance.name,
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp, end = 4.dp),
            style = style,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            text = appliance.cost.toFormattedDecimals(),
            modifier = Modifier
                .padding(end = 4.dp),
            style = style,
            maxLines = 1,
        )

    }

}

@Preview
@Composable
fun AppliancesCostsPreview() {
    val costs = listOf(
        PowerApplianceHour(isBest = true, cost = 18.5, name = "Veļasmašīna", color = "#473592"),
        PowerApplianceHour(),
    )
    LaiksTheme(darkTheme = false) {
        Surface {
            AppliancesCosts(
                appliances = costs,
                modifier = Modifier.width(80.dp)
            )
        }
    }
}
package com.folkmanis.laiks.ui.screens.prices

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.folkmanis.laiks.model.PowerApplianceHour
import com.folkmanis.laiks.ui.theme.LaiksTheme
import com.folkmanis.laiks.utilities.ext.contrasting
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
    ApplianceCostLayout(
        label = {
            Text(
                text = appliance.name,
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp)
                    .wrapContentHeight(),
                style = style,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        cost = {
            Text(
                text = appliance.cost.toFormattedDecimals(),
                modifier = Modifier
                    .padding(end = 4.dp)
                    .wrapContentHeight(),
                style = style,
                maxLines = 1,
            )
        },
        modifier = modifier
            .padding(1.dp)
            .height(18.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(9.dp)
            ),
    )
}

@Composable
fun ApplianceCostLayout(
    label: @Composable () -> Unit,
    cost: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {

    Layout(
        content = {
            label()
            cost()
        },
        modifier = modifier,
    ) { measurables, constrains ->

        val costPlaceable = measurables[1].measure(constrains)

        val labelConstrains = constrains.copy(
            maxWidth = constrains.maxWidth - costPlaceable.width
        )
        val labelPlaceable = measurables[0].measure(labelConstrains)

        val labelBaseline = labelPlaceable[FirstBaseline]
        check(labelBaseline != AlignmentLine.Unspecified)

        val costBaseline = costPlaceable[FirstBaseline]
        check(costBaseline != AlignmentLine.Unspecified)

        layout(
            costPlaceable.width + labelPlaceable.width,
            constrains.maxHeight
        ) {
            val posY = (constrains.maxHeight - labelPlaceable.height) / 2
            labelPlaceable.placeRelative(
                0,
                posY
            )

            costPlaceable.placeRelative(
                labelPlaceable.width,
                posY + labelBaseline - costBaseline
            )

        }
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
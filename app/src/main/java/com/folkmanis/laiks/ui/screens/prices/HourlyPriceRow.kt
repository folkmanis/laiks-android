package com.folkmanis.laiks.ui.screens.prices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.PowerApplianceHour
import com.folkmanis.laiks.model.PricesStatistics
import com.folkmanis.laiks.ui.theme.LaiksTheme
import com.folkmanis.laiks.utilities.composables.TimeIntervalText
import com.folkmanis.laiks.utilities.composables.largeNumberStyle
import com.folkmanis.laiks.utilities.composables.score
import com.folkmanis.laiks.utilities.composables.scoreColor
import com.folkmanis.laiks.utilities.ext.toFormattedDecimals
import com.folkmanis.laiks.utilities.ext.toLocalTime
import com.folkmanis.laiks.utilities.ext.toSignedString
import com.folkmanis.laiks.utilities.ext.toTimestamp
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.random.Random

@Composable
fun HourlyPriceRow(
    offset: Int,
    values: List<NpPrice>,
    modifier: Modifier = Modifier,
    statistics: PricesStatistics?,
    disabled: Boolean = false,
    list: @Composable RowScope.() -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(128.dp)
            .alpha(if (disabled) 0.5f else 1.0f),
    ) {
        Box(
            modifier = Modifier
                .width(48.dp),
        ) {
            Text(
                text = offset.toSignedString(),
                style = largeNumberStyle,
            )
        }

        list()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            values.forEach { npPrice ->
                val value = npPrice.value
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    TimeIntervalText(
                        startTime = npPrice.startTime.toLocalTime(),
                        endTime = npPrice.endTime.toLocalTime(),
                        modifier = Modifier
                            .width(116.dp)
                    )

                    Text(
                        text = value
                            .toFormattedDecimals(),
                        style = largeNumberStyle,
                        color = scoreColor(
                            statistics?.score(value) ?: 0.0,
                            MaterialTheme.colorScheme.surface,
                        ),
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .width(70.dp),
                        textAlign = TextAlign.Right
                    )

                }
            }

        }


    }

}

private val previewAppliances = listOf(
    PowerApplianceHour(
        name = "Veļasmašīna",
        isBest = true,
        cost = 0.185,
    ),
    PowerApplianceHour(),
    PowerApplianceHour(isBest = false),
)


@Preview(showBackground = true)
@Composable
fun HourlyPriceRowPreview() {
    val pricesStart = LocalDateTime.of(2023, 3, 24, 23, 0)
    LaiksTheme {
        HourlyPriceRow(
            offset = 2,
            values = List(4) {
                NpPrice(
                    value = 10.5 + Random.nextDouble(-5.0, 2.0),
                    startTime = pricesStart.plusMinutes(it.toLong() * 15).atZone(
                        ZoneId.systemDefault()
                    ).toTimestamp(),
                    endTime = pricesStart.plusMinutes(it.toLong() * 15 + 15).atZone(
                        ZoneId.systemDefault()
                    ).toTimestamp()
                )
            },
            statistics = PricesStatistics(
                average = 12.5,
                stDev = 0.9
            ),
            disabled = false,
            list = {
                AppliancesCosts(previewAppliances)

            },

            )
    }
}
package com.folkmanis.laiks.ui.screens.prices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.folkmanis.laiks.model.PowerApplianceHour
import com.folkmanis.laiks.ui.theme.LaiksTheme
import com.folkmanis.laiks.utilities.ext.eurMWhToCentsKWh
import com.folkmanis.laiks.utilities.ext.hoursString
import com.folkmanis.laiks.utilities.ext.isDark
import com.folkmanis.laiks.utilities.ext.minutesString
import com.folkmanis.laiks.utilities.ext.toFormattedDecimals
import com.folkmanis.laiks.utilities.ext.toSignedString
import java.time.LocalTime
import kotlin.math.absoluteValue

private val largeNumberStyle = TextStyle(
    fontSize = 24.sp,
    fontWeight = FontWeight.Bold
)

fun PricesStatistics.score(value: Double): Double =
    ((average - value) / stDev).coerceIn(-1.0, 1.0)


fun scoreColor(score: Double, background: Color): Color {
    val positiveHue = 122f
    val negativeHue = 0f
    val hue = if (score >= 0) positiveHue else negativeHue
    val value = if (background.isDark())
        0.5f + 0.5f * score.absoluteValue.toFloat()
    else
        0.8f * score.absoluteValue.toFloat()
    return Color.hsv(hue, 1f, value)
}

@Composable
fun PriceRow(
    offset: Int,
    startTime: LocalTime,
    endTime: LocalTime,
    value: Double,
    statistics: PricesStatistics?,
    modifier: Modifier = Modifier,
    list: @Composable RowScope.() -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(64.dp),
    ) {
        Box(
            modifier = Modifier
                .width(48.dp),
        ) {
            if (offset >= 0) {
                Text(
                    text = offset.toSignedString(),
                    style = largeNumberStyle,
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {

            TimeIntervalText(
                startTime = startTime,
                endTime = endTime,
                modifier = Modifier
                    .width(116.dp)
            )

            list()
        }

        Text(
            text =
            value
                .eurMWhToCentsKWh()
                .toFormattedDecimals(),
            style = largeNumberStyle,
            color = scoreColor(
                statistics?.score(value) ?: 0.0,
                MaterialTheme.colorScheme.surface,
            ),
            modifier = Modifier
                .padding(start = 8.dp)
        )


    }
}

@Composable
fun TimeIntervalText(
    startTime: LocalTime,
    endTime: LocalTime,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = modifier
            .padding(end = 8.dp)
            .fillMaxWidth()
    ) {
        TimeText(time = startTime)
        Text(
            "-",
            style = largeNumberStyle,
        )
        TimeText(time = endTime)
    }

}

@Composable
fun TimeText(
    time: LocalTime,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
    ) {
        Text(
            style = largeNumberStyle,
            text = buildAnnotatedString {
                append(time.hoursString)
                withStyle(
                    SpanStyle(
                        baselineShift = BaselineShift.Superscript,
                        fontSize = 8.sp
                    )
                ) {
                    append(time.minutesString)
                }
            },
            modifier = modifier,
        )
    }
}

@Preview
@Composable
fun PriceRowPreview() {

    val appliances = listOf(
        PowerApplianceHour(
            name = "Veļasmašīna",
            isBest = true,
            cost = 0.185,
        ),
        PowerApplianceHour(),
    )
    LaiksTheme(darkTheme = true) {
        PriceRow(
            value = 12.5,
            offset = 2,
            statistics = PricesStatistics(
                average = 12.5,
                stDev = 0.9
            ),
            startTime = LocalTime.of(12, 0),
            endTime = LocalTime.of(13, 0),
            list = {
                AppliancesCosts(appliances)
            }

        )
    }
}


package com.folkmanis.laiks.ui.screens.prices

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.folkmanis.laiks.model.PowerHour
import com.folkmanis.laiks.ui.theme.LaiksTheme
import com.folkmanis.laiks.utilities.ext.*
import java.time.LocalTime

val largeNumberStyle = TextStyle(
    fontSize = 24.sp,
    fontWeight = FontWeight.Bold
)

@Composable
fun PriceRow(
    powerHour: PowerHour,
    modifier: Modifier = Modifier,
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
            val hours = powerHour.offset
            if (hours >= 0) {
                Text(
                    text = hours.toSignedString(),
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
                startTime = powerHour.startTime.toLocalTime(),
                endTime = powerHour.endTime.toLocalTime(),
                modifier = Modifier
                    .width(116.dp)
            )

            AppliancesCosts(appliances = powerHour.appliancesHours)
        }

        Text(
            text =
            powerHour.price
                .eurMWhToCentsKWh()
                .toFormattedDecimals(),
            style = largeNumberStyle,
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

@Preview(showBackground = true)
@Composable
fun PriceRowPreview() {

    LaiksTheme {
        PriceRow(
            powerHour = PowerHour(
                id = "ACD12",
                offset = 2,
                price = 12.5,
                appliancesHours = listOf(
                    PowerApplianceHour(
                        isBest = true,
                        cost = 0.185,
                        name = "Veļasmašīna"
                    ),
                    PowerApplianceHour(),
                ),
            ),
        )
    }
}

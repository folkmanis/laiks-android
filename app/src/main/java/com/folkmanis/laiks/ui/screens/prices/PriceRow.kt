package com.folkmanis.laiks.ui.screens.prices

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.folkmanis.laiks.ui.theme.LaiksTheme
import com.folkmanis.laiks.VAT
import com.folkmanis.laiks.model.PowerHour
import com.folkmanis.laiks.utilities.ext.*
import java.time.LocalTime

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
                .width(64.dp),
        ) {
            val hours = powerHour.offset
/*                price
                .startTime
                .hoursFrom(dateNow)
 */
            if (hours >= 0) {
                Text(
                    text = hours.toSignedString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                )
            }
        }

        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {

//            Text(
//                price.startTime.toLocalDateString(),
//                fontSize = 18.sp
//            )

            TimeIntervalText(
                startTime = powerHour.startTime.toLocalTime(),
                endTime = powerHour.endTime.toLocalTime(),
            )

            AppliancesCosts(appliances = powerHour.appliancesHours)
        }

        Text(
            text =
            powerHour.price
                .eurMWhToCentsKWh()
                .withVat(VAT)
                .toFormattedDecimals(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 16.dp)
        )


    }
}

@Composable
fun TimeIntervalText(
    modifier: Modifier = Modifier,
    startTime: LocalTime,
    endTime: LocalTime,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 8.dp)
    ) {
        TimeText(time = startTime)
        Text("-")
        TimeText(time = endTime)
    }

}

@Composable
fun TimeText(
    modifier: Modifier = Modifier,
    time: LocalTime,
) {
    Row(modifier = modifier) {
        Text(
            text = time.hoursString,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier,
        )
        Text(
            text = time.minutesString,
            fontSize = 12.sp,
            modifier = Modifier
                .offset(y = 3.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PriceRowPreview() {

    LaiksTheme {
        PriceRow(
            powerHour = PowerHour(id = "ACD12")
        )
    }
}


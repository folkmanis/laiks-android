package com.folkmanis.laiks.ui.screens.prices

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.ui.theme.LaiksTheme
import com.folkmanis.laiks.utilities.VAT
import com.folkmanis.laiks.utilities.ext.*
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime

@Composable
fun PriceRow(
    dateNow: Instant,
    modifier: Modifier = Modifier,
    price: NpPrice,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(48.dp),
    ) {
        Text(
            text = price
                .startTime
                .hoursFrom(dateNow)
                .toSignedString(),
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .width(64.dp),
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth(),
        ) {

            Text(
                price.startTime.toLocalDateString(),
                fontSize = 24.sp
            )

            TimeIntervalText(
                startTime = price.startTime.toLocalTime(),
                endTime = price.endTime.toLocalTime(),
            )

            val formatter = NumberFormat
                .getNumberInstance()
            formatter.maximumFractionDigits = 2
            formatter.minimumFractionDigits = 2

            Text(
                text = formatter
                    .format(price.value.eurMWhToCentsKWh().withVat(VAT)
                    ),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }


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
            dateNow = Instant.now().plusSeconds(-60 * 60 * 10),
            price = NpPrice()
        )
    }
}


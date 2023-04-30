package com.folkmanis.laiks.ui.screens.clock

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.STEP_DOWN_VALUE
import com.folkmanis.laiks.STEP_UP_VALUE
import com.folkmanis.laiks.utilities.ext.hoursString
import com.folkmanis.laiks.utilities.ext.minutesString
import com.folkmanis.laiks.utilities.ext.toSignedString
import java.time.LocalTime

@Composable
fun ClockSelector(
    offset: Int,
    time: LocalTime,
    onOffsetChange: (Int)->Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            LargeFloatingActionButton(
                onClick = { onOffsetChange(STEP_UP_VALUE) },
                modifier = Modifier
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = stringResource(id = R.string.hour_plus_button)
                )
            }

            Text(
                text = offset.toSignedString(),
                fontSize = 64.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )

            LargeFloatingActionButton(
                onClick = { onOffsetChange(STEP_DOWN_VALUE) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.remove),
                    contentDescription = stringResource(id = R.string.hour_minus_button)
                )
            }

        }

        Column(
            modifier = Modifier
                .padding(top = 16.dp),
        ) {
            TimeComponent(
                hours = time.hoursString,
                minutes = time.minutesString,
            )
        }


    }

}

@Composable
fun TimeComponent(
    hours: String,
    minutes: String,
    modifier: Modifier = Modifier
) {

    Row(modifier = modifier) {

        TimeSymbols(text = hours)

        TimeSymbols(
            text = stringResource(id = R.string.minutes_separator),
        )

        TimeSymbols(text = minutes)

    }
}

@Composable
fun TimeSymbols(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        modifier = modifier,
    )
}

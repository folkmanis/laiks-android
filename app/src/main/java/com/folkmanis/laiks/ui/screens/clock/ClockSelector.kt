package com.folkmanis.laiks.ui.screens.clock

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
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
    onOffsetChange: (Int) -> Unit,
    windowHeight: WindowHeightSizeClass,
    modifier: Modifier = Modifier,
) {

    when (windowHeight) {
        WindowHeightSizeClass.Compact -> {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                TimeComponent(
                    hours = time.hoursString,
                    minutes = time.minutesString,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )

                UpDownButtons(
                    offset = offset,
                    onOffsetChange = onOffsetChange
                )

            }
        }

        else -> {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                UpDownButtons(
                    offset = offset,
                    onOffsetChange = onOffsetChange
                )

                TimeComponent(
                    hours = time.hoursString,
                    minutes = time.minutesString,
                    modifier = Modifier
                        .padding(vertical = 16.dp),
                )

            }

        }
    }


}

@Composable
fun UpDownButtons(
    offset: Int,
    onOffsetChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
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

package com.folkmanis.laiks.ui.screens.clock

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.STEP_DOWN_VALUE
import com.folkmanis.laiks.STEP_UP_VALUE
import com.folkmanis.laiks.model.UserPowerAppliance
import com.folkmanis.laiks.ui.theme.LaiksTheme
import com.folkmanis.laiks.utilities.ext.hoursString
import com.folkmanis.laiks.utilities.ext.minutesString
import com.folkmanis.laiks.utilities.ext.toSignedString
import java.time.LocalTime

@Composable
fun ClockSelector(
    offset: Int,
    time: LocalTime,
    onOffsetChange: (Int) -> Unit,
    appliances: List<UserPowerAppliance>,
    onSelected: (idx: Int, name: String) -> Unit,
    isNarrow: Boolean,
    modifier: Modifier = Modifier,
) {
    if (isNarrow) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            AppliancesSelector(
                appliances = appliances,
                onSelected = onSelected,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .weight(1f),
            )

            UpDownButtons(
                offset = offset,
                onOffsetChange = onOffsetChange,
            )

            TimeComponent(
                hours = time.hoursString,
                minutes = time.minutesString,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .weight(1f),
            )

        }
    } else {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                TimeComponent(
                    hours = time.hoursString,
                    minutes = time.minutesString,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                UpDownButtons(
                    offset = offset,
                    onOffsetChange = onOffsetChange,
                    horizontalLayout = true,
                )

            }

            AppliancesSelector(
                appliances = appliances,
                onSelected = onSelected,
                modifier = Modifier.weight(0.67f)
            )

        }
    }

}

@Composable
fun UpDownButtons(
    offset: Int,
    onOffsetChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    horizontalLayout: Boolean = false,
) {

    if (horizontalLayout) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier,
        ) {

            OffsetChangeButton(
                onClick = { onOffsetChange(STEP_DOWN_VALUE) },
                iconId = R.drawable.remove,
                descriptionId = R.string.hour_minus_button,
            )

            OffsetHours(
                offset,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .wrapContentWidth()
            )

            OffsetChangeButton(
                onClick = { onOffsetChange(STEP_UP_VALUE) },
                iconId = R.drawable.add,
                descriptionId = R.string.hour_plus_button,
            )

        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier,
        ) {

            OffsetChangeButton(
                onClick = { onOffsetChange(STEP_UP_VALUE) },
                iconId = R.drawable.add,
                descriptionId = R.string.hour_plus_button,
            )

            OffsetHours(
                offset,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            OffsetChangeButton(
                onClick = { onOffsetChange(STEP_DOWN_VALUE) },
                iconId = R.drawable.remove,
                descriptionId = R.string.hour_minus_button,
            )

        }

    }

}

@Composable
fun OffsetHours(
    offset: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = offset.toSignedString(),
        fontSize = 64.sp,
        fontWeight = FontWeight.ExtraBold,
        modifier = modifier
    )
}

@Composable
fun OffsetChangeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes iconId: Int,
    @StringRes descriptionId: Int,
) {
    LargeFloatingActionButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(iconId),
            contentDescription = stringResource(descriptionId),
        )
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


@Preview
@Composable
fun UpDownButtonsPreviewVertical() {
    LaiksTheme {
        UpDownButtons(
            offset = 2,
            onOffsetChange = {}
        )
    }
}

@Preview
@Composable
fun UpDownButtonsPreviewHorizontal() {
    LaiksTheme {
        UpDownButtons(
            offset = 2,
            onOffsetChange = {},
            horizontalLayout = true
        )
    }
}
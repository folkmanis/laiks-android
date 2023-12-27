package com.folkmanis.laiks.ui.screens.clock

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.data.fake.FakeLaiksUserService
import com.folkmanis.laiks.model.UserPowerAppliance
import com.folkmanis.laiks.utilities.ext.hoursString
import com.folkmanis.laiks.utilities.ext.minutesString
import java.time.LocalTime

@Composable
fun VerticalClockSelector(
    offset: Int,
    time: LocalTime,
    onOffsetChange: (Int) -> Unit,
    appliances: List<UserPowerAppliance>,
    onSelected: (idx: Int, name: String) -> Unit,
    modifier: Modifier = Modifier,
) {

    VerticalClockLayout(
        top = {
            AppliancesSelector(
                appliances = appliances,
                onSelected = onSelected,
            )
        },
        bottom = {
            TimeComponent(
                hours = time.hoursString,
                minutes = time.minutesString,
            )
        },
        large = {
            LargeVerticalUpDownButtons(
                offset = offset,
                onOffsetChange = onOffsetChange,
            )
        },
        normal = {
            VerticalUpDownButtons(
                offset = offset,
                onOffsetChange = onOffsetChange,
            )
        },
        modifier = modifier,
    )
}


@Composable
fun HorizontalClockSelector(
    offset: Int,
    time: LocalTime,
    onOffsetChange: (Int) -> Unit,
    appliances: List<UserPowerAppliance>,
    onSelected: (idx: Int, name: String) -> Unit,
    modifier: Modifier = Modifier,
) {
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

            HorizontalUpDownButtons(
                offset = offset,
                onOffsetChange = onOffsetChange,
            )

        }

        AppliancesSelector(
            appliances = appliances,
            onSelected = onSelected,
            modifier = Modifier.weight(0.67f)
        )

    }
}

@Preview(device = "spec:width=392.7dp,height=400dp,dpi=320")
@Composable
fun VerticalClockSelectorPreview() {
    MaterialTheme {
        VerticalClockSelector(
            offset = 2,
            time = LocalTime.now(),
            onOffsetChange = {},
            appliances = FakeLaiksUserService.laiksUser.appliances,
            onSelected = { _, _ -> },
        )
    }
}

@Preview(device = "spec:width=411dp,height=891dp")
@Composable
fun LargeVerticalClockSelectorPreview() {
    MaterialTheme {
        VerticalClockSelector(
            offset = 2,
            time = LocalTime.now(),
            onOffsetChange = {},
            appliances = FakeLaiksUserService.laiksUser.appliances,
            onSelected = { _, _ -> },
            modifier = Modifier.fillMaxHeight()
        )
    }
}

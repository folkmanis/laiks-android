package com.folkmanis.laiks.ui.screens.clock

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
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
        modifier = modifier,
    ) {

        AppliancesSelector(
            appliances = appliances,
            onSelected = onSelected,
            modifier = Modifier
                .padding(bottom = 16.dp)
//                .weight(1f)
        )

        VerticalUpDownButtons(
            offset = offset,
            onOffsetChange = onOffsetChange,
            modifier = Modifier.layoutId("normal")
        )

        LargeVerticalUpDownButtons(
            offset = offset,
            onOffsetChange = onOffsetChange,
            modifier = Modifier.layoutId("large")
        )

        TimeComponent(
            hours = time.hoursString,
            minutes = time.minutesString,
            modifier = Modifier
                .padding(vertical = 16.dp)
//                .weight(1f),
        )

    }
}

@Composable
fun VerticalClockLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val constrainedHeight=constraints.copy(
            minHeight = 0
        )
        Log.d(TAG, "maxHeight: ${constraints.maxHeight}")
        val topPlaceables = measurables
            .takeWhile { it.layoutId == null }
            .map { it.measure(constrainedHeight) }
        val bottomPlaceables = measurables
            .takeLastWhile { it.layoutId == null }
            .map { it.measure(constrainedHeight) }
        val normalPlaceable = measurables
            .find { it.layoutId == "normal" }
            ?.measure(constrainedHeight)
        val largePlaceable = measurables
            .find { it.layoutId == "large" }
            ?.measure(constrainedHeight)
        val largeHeight = topPlaceables.sumOf { it.height } +
                bottomPlaceables.sumOf { it.height } +
                (largePlaceable?.height ?: 0)
        val normalHeight = topPlaceables.sumOf { it.height } +
                bottomPlaceables.sumOf { it.height } +
                (normalPlaceable?.height ?: 0)
        val isLarge = largeHeight >= constraints.maxHeight
        Log.d(TAG, "largeHeight: ${largeHeight}")
        Log.d(TAG, "normalHeight: ${normalHeight}")

        val width = if (isLarge) largeHeight else normalHeight
        val height = constraints.maxHeight

        layout(width, height) {
            var yPos = 0
            val center = width / 2
            topPlaceables.forEach { placeable ->
                placeable.placeRelative(
                    center - placeable.width / 2,
                    yPos
                )
                yPos += placeable.height
            }

            if (isLarge) {
                largePlaceable?.also { placeable ->
                    placeable.placeRelative(
                        center - placeable.width / 2,
                        yPos
                    )
                    yPos += placeable.height
                }
            } else {
                normalPlaceable?.also { placeable ->
                    placeable.placeRelative(
                        center - placeable.width / 2,
                        yPos
                    )
                    yPos += placeable.height
                }
            }

            bottomPlaceables.forEach { placeable ->
                placeable.placeRelative(
                    center - placeable.width / 2,
                    yPos
                )
                yPos += placeable.height
            }
        }
    }
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

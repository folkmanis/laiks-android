@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package com.folkmanis.laiks.ui.screens.clock

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.data.fake.FakeLaiksUserService
import com.folkmanis.laiks.model.UserPowerAppliance
import com.folkmanis.laiks.ui.theme.LaiksTheme
import com.folkmanis.laiks.utilities.ext.hoursString
import com.folkmanis.laiks.utilities.ext.minutesString
import java.time.LocalTime

fun Modifier.pricesButtonOffset(isLarge: Boolean): Modifier {
    val off = if (isLarge) (-32).dp else (-16).dp
    return this then Modifier.offset(off, off)
}

@Composable
internal fun ClockScreen(
    hours: String,
    minutes: String,
    offset: Int,
    pricesAllowed: Boolean,
    appliances: List<UserPowerAppliance>,
    updateOffset: (Int) -> Unit,
    onShowPrices: () -> Unit,
    onShowAppliance: (idx: Int, name: String) -> Unit,
    windowSize: WindowSizeClass,
    modifier: Modifier = Modifier,
) {

    val isNarrow = windowSize.widthSizeClass == WindowWidthSizeClass.Compact
            || windowSize.heightSizeClass >= WindowHeightSizeClass.Medium

    val isLarge = windowSize.widthSizeClass > WindowWidthSizeClass.Compact
            || windowSize.heightSizeClass > WindowHeightSizeClass.Medium

    val allowedAppliances = remember(pricesAllowed, appliances) {
        if (pricesAllowed) appliances else emptyList()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {

        when {
            isNarrow -> VerticalClockSelector(
                offset = offset,
                hours = hours,
                minutes = minutes,
                onOffsetChange = updateOffset,
                appliances = allowedAppliances,
                onSelected = onShowAppliance,
                modifier = Modifier.align(Alignment.Center),
            )

            else -> HorizontalClockSelector(
                offset = offset,
                hours = hours,
                minutes = minutes,
                onOffsetChange = updateOffset,
                appliances = allowedAppliances,
                onSelected = onShowAppliance,
                modifier = Modifier.align(Alignment.Center),
            )
        }

        if (pricesAllowed) {
            PricesButton(
                onClick = onShowPrices,
                isLarge = isLarge,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .pricesButtonOffset(isLarge)
            )
        }


    }
}

@Preview(
    showBackground = true,
    device = "spec:width=320dp,height=480dp,dpi=320"
)
@Composable
fun ClockScreenPreview() {
    var offset by remember {
        mutableIntStateOf(2)
    }
    LaiksTheme {
        ClockScreen(
            hours = LocalTime.now().plusHours(offset.toLong()).hoursString,
            minutes = LocalTime.now().minutesString,
            offset = offset,
            pricesAllowed = true,
            appliances = FakeLaiksUserService.laiksUser.appliances,
            onShowPrices = {},
            updateOffset = { offset += it },
            onShowAppliance = { _, _ -> },
            windowSize = WindowSizeClass.calculateFromSize(
                size = DpSize(width = 320.dp, height = 480.dp)
            ),
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=392.7dp,height=850.9dp,dpi=440,orientation=landscape"
)
@Composable
fun ClockScreenPreviewLandscape() {
    var offset by remember {
        mutableIntStateOf(2)
    }
    LaiksTheme {
        ClockScreen(
            hours = LocalTime.now().plusHours(offset.toLong()).hoursString,
            minutes = LocalTime.now().minutesString,
            offset = offset,
            pricesAllowed = true,
            appliances = FakeLaiksUserService.laiksUser.appliances,
            onShowPrices = {},
            updateOffset = { offset += it },
            onShowAppliance = { _, _ -> },
            windowSize = WindowSizeClass.calculateFromSize(
                size = DpSize(width = 860.dp, height = 400.dp)
            )
        )
    }
}


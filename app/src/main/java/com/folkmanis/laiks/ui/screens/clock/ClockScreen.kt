@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package com.folkmanis.laiks.ui.screens.clock

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.fake.FakeLaiksUserService
import com.folkmanis.laiks.model.UserPowerAppliance
import com.folkmanis.laiks.ui.theme.LaiksTheme
import com.folkmanis.laiks.utilities.ext.hoursString
import com.folkmanis.laiks.utilities.ext.minutesString
import java.time.LocalTime

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

    val isSmall = windowSize.widthSizeClass == WindowWidthSizeClass.Compact
            && windowSize.heightSizeClass <= WindowHeightSizeClass.Medium
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
                isSmall = isSmall,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-16).dp, y = (-16).dp)
            )
        }


    }
}

@Composable
fun PricesButton(
    onClick: () -> Unit,
    isSmall: Boolean,
    modifier: Modifier = Modifier,
) {
    if (isSmall) {
        SmallPricesButton(
            onClick = onClick,
            modifier = modifier
        )
    } else {
        LargePricesButton(
            onClick = onClick,
            modifier = modifier
                .offset(x = (-16).dp, y = (-16).dp)
        )
    }

}

@Composable
fun SmallPricesButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(R.drawable.euro_symbol_48px),
            contentDescription = stringResource(R.string.show_prices_button),
            modifier = Modifier.size(24.dp),
        )
    }
}

@Composable
fun LargePricesButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        icon = {
            Icon(
                painter = painterResource(R.drawable.euro_symbol_48px),
                contentDescription = stringResource(R.string.show_prices_button),
                modifier = Modifier.size(24.dp),
            )
        },
        text = {
            Text(
                text = stringResource(R.string.show_prices_button),
            )
        }
    )

}


@Preview(
    showBackground = true,
    device = "spec:width=320dp,height=480dp,dpi=320"
)
@Composable
fun ClockScreenPreview() {
    LaiksTheme {
        ClockScreen(
            hours = LocalTime.now().hoursString,
            minutes = LocalTime.now().minutesString,
            offset = 2,
            pricesAllowed = true,
            appliances = FakeLaiksUserService.laiksUser.appliances,
            onShowPrices = {},
            updateOffset = {},
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
    LaiksTheme {
        ClockScreen(
            hours = LocalTime.now().hoursString,
            minutes = LocalTime.now().minutesString,
            offset = 2,
            pricesAllowed = true,
            appliances = FakeLaiksUserService.laiksUser.appliances,
            onShowPrices = {},
            updateOffset = {},
            onShowAppliance = { _, _ -> },
            windowSize = WindowSizeClass.calculateFromSize(
                size = DpSize(width = 860.dp, height = 400.dp)
            )
        )
    }
}


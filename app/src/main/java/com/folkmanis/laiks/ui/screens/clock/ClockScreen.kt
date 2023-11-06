package com.folkmanis.laiks.ui.screens.clock

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.fake.FakeLaiksUserService
import com.folkmanis.laiks.model.UserPowerAppliance
import com.folkmanis.laiks.ui.theme.LaiksTheme
import java.time.LocalTime

@Composable
internal fun ClockScreen(
    time: LocalTime,
    offset: Int,
    pricesAllowed: Boolean,
    appliances: List<UserPowerAppliance>,
    updateOffset: (Int) -> Unit,
    onShowPrices: () -> Unit,
    onShowAppliance: (idx: Int, name: String) -> Unit,
    windowWidth: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
) {

    val isNarrow = windowWidth == WindowWidthSizeClass.Compact

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {

        ClockSelector(
            offset = offset,
            time = time,
            onOffsetChange = updateOffset,
            appliances = appliances,
            onSelected = onShowAppliance,
            isNarrow = isNarrow,
            modifier = Modifier.align(Alignment.Center),
        )

        if (pricesAllowed) {
            FloatingActionButton(
                onClick = onShowPrices,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-16).dp, y = (-16).dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.euro_symbol_48px),
                    contentDescription = stringResource(R.string.show_prices_button),
                    modifier = Modifier.size(24.dp),
                )
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun ClockScreenPreview() {
    LaiksTheme {
        ClockScreen(
            time = LocalTime.now(),
            offset = 2,
            pricesAllowed = true,
            appliances = FakeLaiksUserService.laiksUser.appliances,
            onShowPrices = {},
            updateOffset = {},
            onShowAppliance = { _, _ -> },
            windowWidth = WindowWidthSizeClass.Compact,
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:parent=pixel_5," +
            "orientation=landscape"
)
@Composable
fun ClockScreenPreviewLandscape() {
    LaiksTheme {
        ClockScreen(
            time = LocalTime.now(),
            offset = 2,
            pricesAllowed = true,
            appliances = FakeLaiksUserService.laiksUser.appliances,
            onShowPrices = {},
            updateOffset = {},
            onShowAppliance = { _, _ -> },
            windowWidth = WindowWidthSizeClass.Medium,
        )
    }
}


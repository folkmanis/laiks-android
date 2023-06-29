package com.folkmanis.laiks.ui.screens.clock

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.folkmanis.laiks.data.domain.PowerApplianceRecord
import com.folkmanis.laiks.data.fake.FakeAppliancesService
import com.folkmanis.laiks.ui.theme.LaiksTheme
import java.time.LocalTime

@Composable
internal fun ClockScreen(
    time: LocalTime,
    offset: Int,
    pricesAllowed: Boolean,
    appliances: List<PowerApplianceRecord>,
    updateOffset: (Int) -> Unit,
    onShowPrices: () -> Unit,
    onShowAppliance: (PowerApplianceRecord) -> Unit,
    modifier: Modifier = Modifier,
    windowHeight: WindowHeightSizeClass,
) {


    Box(
        modifier = modifier
            .fillMaxSize()
    ) {

        ClockSelector(
            offset = offset,
            time = time,
            onOffsetChange = updateOffset,
            windowHeight = windowHeight,
            modifier = Modifier.align(Alignment.Center),
        )

        if (pricesAllowed) {
            PricesSelector(
                appliances = appliances,
                onSelected = onShowAppliance,
                onShowPrices = onShowPrices,
            )
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
            appliances = FakeAppliancesService.testApplianceRecords,
            onShowPrices = {},
            updateOffset = {},
            onShowAppliance = {},
            windowHeight = WindowHeightSizeClass.Medium,
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
            appliances = FakeAppliancesService.testApplianceRecords,
            onShowPrices = {},
            updateOffset = {},
            onShowAppliance = {},
            windowHeight = WindowHeightSizeClass.Compact,
        )
    }
}


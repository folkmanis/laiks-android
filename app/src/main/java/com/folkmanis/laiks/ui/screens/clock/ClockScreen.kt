package com.folkmanis.laiks.ui.screens.clock

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.fake.FakeAppliancesService
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.ui.theme.LaiksTheme
import java.time.LocalTime

@Composable
internal fun ClockScreen(
    time: LocalTime,
    offset: Int,
    pricesAllowed: Boolean,
    appliances: List<PowerAppliance>,
    updateOffset: (Int) -> Unit,
    onShowPrices: () -> Unit,
    onShowAppliance: (idx: Int, name: String) -> Unit,
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
            appliances = appliances,
            onSelected = onShowAppliance,
            modifier = Modifier.align(Alignment.Center),
        )

        if (pricesAllowed) {
            FloatingActionButton(
                onClick = onShowPrices,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.euro_symbol_48px),
                    contentDescription = null,
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
            appliances = FakeAppliancesService.testAppliances,
            onShowPrices = {},
            updateOffset = {},
            onShowAppliance = { _, _ -> },
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
            appliances = FakeAppliancesService.testAppliances,
            onShowPrices = {},
            updateOffset = {},
            onShowAppliance = { _, _ -> },
            windowHeight = WindowHeightSizeClass.Compact,
        )
    }
}


package com.folkmanis.laiks.ui.screens.clock

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
    onShowAppliance: (PowerAppliance) -> Unit,
    modifier: Modifier = Modifier,
    windowHeight: WindowHeightSizeClass,
) {

        Box(
            modifier = modifier
                .fillMaxSize()
        ) {

            when (windowHeight) {
                WindowHeightSizeClass.Compact ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        if (pricesAllowed) {
                            AppliancesSelector(
                                appliances = appliances,
                                onSelected = onShowAppliance,
                            )
                        }

                        ClockSelector(
                            offset = offset,
                            time = time,
                            onOffsetChange = updateOffset,
                        )

                    }

                else ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Box(
                            modifier = Modifier.weight(1f)
                        ) {
                            if (pricesAllowed) {
                                AppliancesSelector(
                                    appliances = appliances,
                                    onSelected = onShowAppliance,
                                    modifier = Modifier.fillMaxHeight()
                                )
                            }
                        }

                        ClockSelector(
                            offset = offset,
                            time = time,
                            onOffsetChange = updateOffset
                        )

                        Spacer(modifier = Modifier.weight(1f))

                    }

            }


            if (pricesAllowed) {
                FloatingActionButton(
                    onClick = onShowPrices,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomEnd)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.euro_symbol_48px),
                        contentDescription = stringResource(id = R.string.show_prices_button),
                        modifier = Modifier.size(24.dp)
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
            appliances = FakeAppliancesService.testAppliances,
            onShowPrices = {},
            updateOffset = {},
            onShowAppliance = {},
            windowHeight = WindowHeightSizeClass.Compact,
        )
    }
}


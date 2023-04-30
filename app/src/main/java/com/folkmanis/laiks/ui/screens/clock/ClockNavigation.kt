package com.folkmanis.laiks.ui.screens.clock

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.folkmanis.laiks.R
import com.folkmanis.laiks.model.PowerAppliance
import java.time.LocalTime

const val CLOCK_ROUTE = "Clock"

fun NavGraphBuilder.clockScreen(
    onNavigateToPrices: () -> Unit,
    onNavigateToAppliance: (PowerAppliance) -> Unit,
    windowHeight: WindowHeightSizeClass,
    setTitle: (String) -> Unit
) {

    composable(CLOCK_ROUTE) {

        val title = stringResource(id = R.string.app_name)
        LaunchedEffect(Unit) {
            setTitle(title)
        }

        val viewModel: ClockViewModel = hiltViewModel()

        val isPricesAllowed by viewModel.isPricesAllowed
            .collectAsStateWithLifecycle(initialValue = false)
        val appliances by viewModel.appliances
            .collectAsStateWithLifecycle(initialValue = emptyList())
        val time by viewModel.timeState
            .collectAsStateWithLifecycle(initialValue = LocalTime.now())
        val offset by viewModel.offsetState
            .collectAsStateWithLifecycle()

        ClockScreen(
            time = time,
            offset = offset,
            pricesAllowed = isPricesAllowed,
            appliances = appliances,
            onShowPrices = onNavigateToPrices,
            updateOffset = viewModel::updateOffset,
            onShowAppliance = onNavigateToAppliance,
            windowHeight = windowHeight,
        )

    }

}

fun NavController.navigateToClock(builder: NavOptionsBuilder.() -> Unit = {}) {
    this.navigate(CLOCK_ROUTE, builder)
}

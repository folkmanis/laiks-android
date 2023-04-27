package com.folkmanis.laiks.ui.screens.clock

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.folkmanis.laiks.LaiksAppState
import com.folkmanis.laiks.model.PowerAppliance
import java.time.LocalTime

const val CLOCK_ROUTE = "Clock"

fun NavGraphBuilder.clockScreen(
    appState: LaiksAppState,
    onNavigateToPrices: () -> Unit,
    onNavigateToAppliance: (PowerAppliance) -> Unit,
) {

    composable(CLOCK_ROUTE) {

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
            actions = { appState.AppUserMenu() },
            onShowAppliance = onNavigateToAppliance,
            windowHeight = appState.windowSize.heightSizeClass,
        )

    }

}

fun NavController.navigateToClock(builder: NavOptionsBuilder.() -> Unit = {}) {
    this.navigate(CLOCK_ROUTE, builder)
}

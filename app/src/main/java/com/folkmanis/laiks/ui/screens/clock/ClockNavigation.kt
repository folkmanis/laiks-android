package com.folkmanis.laiks.ui.screens.clock

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.folkmanis.laiks.LaiksAppState
import com.folkmanis.laiks.model.PowerAppliance

const val CLOCK_ROUTE = "Clock"

fun NavGraphBuilder.clockScreen(
    appState: LaiksAppState,
    onNavigateToPrices: () -> Unit,
    onNavigateToAppliance: (PowerAppliance)->Unit,
    windowSize: WindowSizeClass,
) {

    composable(CLOCK_ROUTE) {

        val viewModel: ClockViewModel = hiltViewModel()
        val uiState by viewModel
            .uiState
            .collectAsStateWithLifecycle()
        val pricesAllowed by viewModel.isPricesAllowed.collectAsStateWithLifecycle(initialValue = false)
        val appliances by viewModel.appliances.collectAsStateWithLifecycle(initialValue = emptyList())

        ClockScreen(
            uiState = uiState,
            pricesAllowed = pricesAllowed,
            onShowPrices = onNavigateToPrices,
            updateOffset = viewModel::updateOffset,
            actions = { appState.AppUserMenu() },
            onShowAppliance = onNavigateToAppliance,
            appliances = appliances,
            windowHeight = windowSize.heightSizeClass,
        )

    }

}

fun NavController.navigateToClock(builder: NavOptionsBuilder.() -> Unit = {}) {
    this.navigate(CLOCK_ROUTE, builder)
}

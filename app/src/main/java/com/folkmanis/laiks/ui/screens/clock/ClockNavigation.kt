package com.folkmanis.laiks.ui.screens.clock

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.folkmanis.laiks.LaiksAppState

const val CLOCK_ROUTE = "Clock"

fun NavGraphBuilder.clockScreen(
    appState: LaiksAppState,
    onNavigateToPrices: () -> Unit,
) {

    composable(CLOCK_ROUTE) {

        val viewModel: ClockViewModel = hiltViewModel()
        val uiState by viewModel
            .uiState
            .collectAsStateWithLifecycle()
        val pricesAllowed by viewModel.isPricesAllowed.collectAsStateWithLifecycle(initialValue = false)

        ClockScreen(
            uiState = uiState,
            pricesAllowed = pricesAllowed,
            onShowPrices = onNavigateToPrices,
            updateOffset = viewModel::updateOffset,
            actions = { appState.AppUserMenu() },
        )

    }

}

fun NavController.navigateToClock(builder: NavOptionsBuilder.() -> Unit = {}) {
    this.navigate(CLOCK_ROUTE, builder)
}

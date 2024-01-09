package com.folkmanis.laiks.ui.screens.clock

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.folkmanis.laiks.R

const val CLOCK_ROUTE = "Clock"

fun NavGraphBuilder.clockScreen(
    onNavigateToPrices: () -> Unit,
    onNavigateToAppliance: (Int, String) -> Unit,
    windowSize: WindowSizeClass,
    setTitle: (String) -> Unit
) {

    composable(CLOCK_ROUTE) {

        val title = stringResource(id = R.string.app_name)
        LaunchedEffect(title, setTitle) {
            setTitle(title)
        }

        val viewModel: ClockViewModel = hiltViewModel()

        val uiState = viewModel.uiState

        LaunchedEffect(Unit) {
            viewModel.initialize()
        }

        ClockScreen(
            hours = uiState.hours,
            minutes = uiState.minutes,
            offset = uiState.offset,
            pricesAllowed = uiState.isPricesAllowed,
            appliances = uiState.appliances,
            onShowPrices = onNavigateToPrices,
            updateOffset = viewModel::updateOffset,
            onShowAppliance = onNavigateToAppliance,
            windowSize = windowSize,
        )

    }

}

fun NavController.navigateToClockSingleTop(builder: NavOptionsBuilder.() -> Unit = {}) {
    this.navigate(CLOCK_ROUTE) {
        launchSingleTop = true
        popUpTo(0) { inclusive = true }
        builder()
    }
}

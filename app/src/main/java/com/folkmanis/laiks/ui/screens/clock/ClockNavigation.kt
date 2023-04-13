package com.folkmanis.laiks.ui.screens.clock

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.folkmanis.laiks.LaiksAppState
import com.folkmanis.laiks.LaiksScreen
import com.folkmanis.laiks.ui.screens.user_menu.UserMenu

private const val ROUTE = "Clock"

fun NavGraphBuilder.clockScreen(
    appState: LaiksAppState,
    onNavigateToPrices: () -> Unit,
) {

    composable(ROUTE) {

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
            actions = { appState.AppUserMenu() }
        )

    }

}

fun NavController.navigateToClock(navOptions: NavOptions? = null) {
    this.navigate(ROUTE, navOptions)
}

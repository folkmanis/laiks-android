package com.folkmanis.laiks.ui.screens.prices

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.folkmanis.laiks.LaiksAppState

const val ROUTE = "Prices"

fun NavGraphBuilder.pricesScreen(
    state: LaiksAppState
) {

    composable(ROUTE) {

        val viewModel: PricesViewModel = hiltViewModel()
        val uiState = viewModel.uiState
            .collectAsStateWithLifecycle(PricesUiState.Loading)
            .value

        PricesScreen(
            state = uiState,
            actions = { state.AppUserMenu() },
            popUp = state::navigateToDefault,
        )

    }
}

fun NavController.navigateToPrices(navOptions: NavOptions? = null) {
    navigate(ROUTE, navOptions)
}
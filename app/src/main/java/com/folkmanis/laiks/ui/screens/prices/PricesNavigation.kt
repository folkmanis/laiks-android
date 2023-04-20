package com.folkmanis.laiks.ui.screens.prices

import androidx.compose.runtime.getValue
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
        val npUploadAllowed by viewModel.npUploadAllowed
            .collectAsStateWithLifecycle(initialValue = false)
        val statistics by viewModel.pricesStatistics
            .collectAsStateWithLifecycle(initialValue = PricesStatistics())

        PricesScreen(
            state = uiState,
            npUploadAllowed = npUploadAllowed,
            actions = { state.AppUserMenu() },
            popUp = state::popUp,
            onRefresh = viewModel::updateNpPrices,
            statistics = statistics,
        )

    }
}

fun NavController.navigateToPrices(navOptions: NavOptions? = null) {
    navigate(ROUTE, navOptions)
}
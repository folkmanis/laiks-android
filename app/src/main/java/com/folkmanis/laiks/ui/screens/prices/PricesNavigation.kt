package com.folkmanis.laiks.ui.screens.prices

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.folkmanis.laiks.R

const val ROUTE = "Prices"

fun NavGraphBuilder.pricesScreen(
    setTitle: (String) -> Unit,
    onSetMarketZone: () -> Unit,
) {

    composable(ROUTE) {

        val viewModel: PricesViewModel = hiltViewModel()

        LaunchedEffect(onSetMarketZone) {
            viewModel.initialize(onSetMarketZone)
        }

        val title = stringResource(id = R.string.prices_screen)
        LaunchedEffect(title, setTitle) {
            setTitle(title)
        }

        val uiState by viewModel.uiState
            .collectAsStateWithLifecycle(PricesUiState.Loading)
        val statistics by viewModel.pricesStatistics
            .collectAsStateWithLifecycle(initialValue = null)
        val appliances by viewModel.appliancesState
            .collectAsStateWithLifecycle(initialValue = emptyMap())

        PricesScreen(
            state = uiState,
            statistics = statistics,
            appliances = appliances
        )

    }
}

fun NavController.navigateToPrices(navOptions: NavOptions? = null) {
    navigate(ROUTE, navOptions)
}

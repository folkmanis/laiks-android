package com.folkmanis.laiks.ui.screens.prices

import androidx.compose.runtime.Composable
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
    onMarketZoneNotSet: () -> Unit,
) {

    composable(ROUTE) {

        val viewModel: PricesViewModel = hiltViewModel()

        val marketZoneId by viewModel.currentMarketZoneId
            .collectAsStateWithLifecycle(initialValue = "")

        val title = getTitle(marketZoneId = marketZoneId)
        LaunchedEffect(title, setTitle) {
            setTitle(title)
        }

        val appliances by viewModel.appliancesState
            .collectAsStateWithLifecycle(initialValue = emptyMap())

        LaunchedEffect(Unit) {
            viewModel.initialize()
        }

        PricesScreen(
            state = viewModel.uiState,
            statistics = viewModel.pricesStatistics,
            appliances = appliances,
            onMarketZoneSet = { viewModel.initialize() },
            onMarketZoneNotSet = onMarketZoneNotSet,
        )

    }
}

fun NavController.navigateToPrices(navOptions: NavOptions? = null) {
    navigate(ROUTE, navOptions)
}

@Composable
fun getTitle(marketZoneId: String): String {
    val heading = stringResource(id = R.string.prices_screen)
    return if (marketZoneId.isNotEmpty())
        "$marketZoneId $heading"
    else
        heading
}
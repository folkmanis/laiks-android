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
import kotlinx.coroutines.flow.Flow

const val ROUTE = "Prices"

fun NavGraphBuilder.pricesScreen(
    setTitle: (String) -> Unit,
    onSetMarketZone: () -> Unit,
) {

    composable(ROUTE) {

        val viewModel: PricesViewModel = hiltViewModel()

        val title = getTitle(marketZoneIdFlow = viewModel.currentMarketZoneId)
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
            appliances = appliances,
            onSetMarketZone = onSetMarketZone,
        )

    }
}

fun NavController.navigateToPrices(navOptions: NavOptions? = null) {
    navigate(ROUTE, navOptions)
}

@Composable
fun getTitle(marketZoneIdFlow: Flow<String>): String {
    val heading = stringResource(id = R.string.prices_screen)
    val marketZoneId by marketZoneIdFlow
        .collectAsStateWithLifecycle(initialValue = "")
    return if (marketZoneId.isNotEmpty())
        "$marketZoneId $heading"
    else
        heading
}
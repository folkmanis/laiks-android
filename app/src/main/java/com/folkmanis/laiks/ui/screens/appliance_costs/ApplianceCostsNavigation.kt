package com.folkmanis.laiks.ui.screens.appliance_costs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.folkmanis.laiks.R

private const val ROUTE = "ApplianceCosts"
internal const val APPLIANCE_IDX = "applianceIdx"
internal const val APPLIANCE_NAME = "name"

fun NavGraphBuilder.applianceCostsScreen(
    setTitle: (String) -> Unit,
    onSetMarketZone: () -> Unit,
) {

    composable(
        route = "$ROUTE/{$APPLIANCE_IDX}?$APPLIANCE_NAME={$APPLIANCE_NAME}",
    ) {

        val viewModel: ApplianceCostsViewModel = hiltViewModel()
        val state by viewModel.uiState.collectAsStateWithLifecycle()

        val title = applianceTitle(state = state)
        LaunchedEffect(title, setTitle) {
            setTitle(title)
        }

        ApplianceCostsScreen(
            state = state,
            setTitle = setTitle,
            onSetMarketZone = onSetMarketZone,
        )

    }

}

fun NavController.applianceCosts(
    applianceIdx: Int,
    name: String,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(
        "$ROUTE/${applianceIdx}?$APPLIANCE_NAME=${name}",
        builder
    )
}

@Composable
fun applianceTitle(state: ApplianceCostsUiState): String {
    return when (state) {
        is ApplianceCostsUiState.Loading -> state.name
            ?: stringResource(id = R.string.appliance_screen)

        is ApplianceCostsUiState.Success -> state.name

        else -> stringResource(id = R.string.appliance_screen)
    }
}
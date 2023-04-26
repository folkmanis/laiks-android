package com.folkmanis.laiks.ui.screens.appliance_costs

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.folkmanis.laiks.LaiksAppState
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.ui.screens.prices.ObserveLifecycle

private const val ROUTE = "ApplianceCosts"
private const val APPLIANCE_ID = "applianceId"

fun NavGraphBuilder.applianceCostsScreen(
    appState: LaiksAppState
) {

    composable(
        route = "$ROUTE/{$APPLIANCE_ID}",
        arguments = listOf(
            navArgument(APPLIANCE_ID) {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->
        val applianceId = backStackEntry.arguments?.getString(APPLIANCE_ID)

        val viewModel: ApplianceCostsViewModel = hiltViewModel()
        val state by viewModel.uiState.collectAsStateWithLifecycle(ApplianceCostsUiState.Loading)

        viewModel.ObserveLifecycle(LocalLifecycleOwner.current.lifecycle)

        LaunchedEffect(applianceId) {
            viewModel.setApplianceId(applianceId)
        }

        ApplianceCostsScreen(
            state = state,
            statistics = null,
            snackbarHostState = appState.snackbarHostState,
            actions = { appState.AppUserMenu() },
            popUp = appState::popUp
        )

    }

}

fun NavController.navigateToApplianceCosts(
    appliance: PowerAppliance,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    navigate("$ROUTE/${appliance.id}", builder)
}
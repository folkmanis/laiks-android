package com.folkmanis.laiks.ui.screens.appliance_costs

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
private const val APPLIANCE_NAME = "name"

fun NavGraphBuilder.applianceCostsScreen(
    appState: LaiksAppState
) {

    composable(
        route = "$ROUTE/{$APPLIANCE_ID}",
        arguments = listOf(
            navArgument(APPLIANCE_ID) {
                type = NavType.StringType
            },
            navArgument(APPLIANCE_NAME) {
                type = NavType.StringType
                defaultValue = ""
            }
        )
    ) { backStackEntry ->
        val applianceId = backStackEntry.arguments?.getString(APPLIANCE_ID)

        val viewModel: ApplianceCostsViewModel = hiltViewModel()
        val state by viewModel.uiState.collectAsStateWithLifecycle(ApplianceCostsUiState.Loading)
        val statistics by viewModel.statistics.collectAsStateWithLifecycle(initialValue = null)
        val name by viewModel.nameState.collectAsStateWithLifecycle(
            initialValue = backStackEntry.arguments?.getString(APPLIANCE_NAME)
        )

        viewModel.ObserveLifecycle(LocalLifecycleOwner.current.lifecycle)

        viewModel.setApplianceId(applianceId)

        ApplianceCostsScreen(
            state = state,
            name = name,
            statistics = statistics,
            snackbarHostState = appState.snackbarHostState,
            actions = { appState.AppUserMenu() },
            popUp = appState::popUp,
        )

    }

}

fun NavController.navigateToApplianceCosts(
    appliance: PowerAppliance,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    navigate("$ROUTE/${appliance.id}?$APPLIANCE_NAME=${appliance.name}", builder)
}
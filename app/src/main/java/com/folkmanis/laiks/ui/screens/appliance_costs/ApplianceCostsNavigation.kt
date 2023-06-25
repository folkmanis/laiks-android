package com.folkmanis.laiks.ui.screens.appliance_costs

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.domain.PowerApplianceRecord

private const val ROUTE = "ApplianceCosts"
private const val APPLIANCE_TYPE = "type"
private const val APPLIANCE_ID = "id"
private const val APPLIANCE_NAME = "name"

fun NavGraphBuilder.applianceCostsScreen(
    setTitle: (String) -> Unit,
) {

    composable(
        route = "$ROUTE/{$APPLIANCE_TYPE}/{$APPLIANCE_ID}",
        arguments = listOf(
            navArgument(APPLIANCE_TYPE) {
                type = NavType.IntType
                defaultValue = 0
            },
            navArgument(APPLIANCE_ID) {
                type = NavType.StringType
            },
            navArgument(APPLIANCE_NAME) {
                type = NavType.StringType
                defaultValue = ""
            },
        )
    ) { backStackEntry ->
        val applianceType = backStackEntry.arguments?.getInt(APPLIANCE_TYPE)
        val applianceId = backStackEntry.arguments?.getString(APPLIANCE_ID)

        val viewModel: ApplianceCostsViewModel = hiltViewModel()
        val state by viewModel.uiState.collectAsStateWithLifecycle(ApplianceCostsUiState.Loading)
        val statistics by viewModel.statistics.collectAsStateWithLifecycle(initialValue = null)
        val name by viewModel.nameState.collectAsStateWithLifecycle(
            initialValue = backStackEntry.arguments?.getString(APPLIANCE_NAME)
        )

        val title = name ?: stringResource(id = R.string.appliance_screen)
        LaunchedEffect(title) {
            setTitle(title)
        }

//        viewModel.ObserveLifecycle(LocalLifecycleOwner.current.lifecycle)

        viewModel.setAppliance(applianceType, applianceId)

        ApplianceCostsScreen(
            state = state,
            statistics = statistics,
        )

    }

}

fun NavController.applianceCosts(
    appliance: PowerApplianceRecord,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(
        "$ROUTE/${appliance.type}/${appliance.id}?$APPLIANCE_NAME=${appliance.name}",
        builder
    )
}
package com.folkmanis.laiks.ui.screens.appliance_costs

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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

private const val ROUTE = "ApplianceCosts"
internal const val APPLIANCE_IDX = "applianceIdx"
internal const val APPLIANCE_NAME = "name"

fun NavGraphBuilder.applianceCostsScreen(
    setTitle: (String) -> Unit,
) {

    composable(
        route = "$ROUTE/{$APPLIANCE_IDX}?$APPLIANCE_NAME={$APPLIANCE_NAME}",
        arguments = listOf(
            navArgument(APPLIANCE_IDX) {
                type = NavType.IntType
            },
            navArgument(APPLIANCE_NAME) {
                type = NavType.StringType
                defaultValue = ""
            },
        )
    ) { backStackEntry ->

        val applianceIdx = remember(backStackEntry) {
            backStackEntry.arguments?.getInt(APPLIANCE_IDX)
        }
        val initialName = remember(backStackEntry) {
            backStackEntry.arguments?.getString(APPLIANCE_NAME)
        }

        val viewModel: ApplianceCostsViewModel = hiltViewModel()
        val state by viewModel.uiState.collectAsStateWithLifecycle(ApplianceCostsUiState.Loading)

        val title = initialName ?: stringResource(id = R.string.appliance_screen)
        LaunchedEffect(title) {
            setTitle(title)
        }

        LaunchedEffect(applianceIdx) {
            viewModel.setIdx(applianceIdx)
        }
//        viewModel.ObserveLifecycle(LocalLifecycleOwner.current.lifecycle)


        ApplianceCostsScreen(
            state = state,
            setTitle = setTitle,
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
package com.folkmanis.laiks.ui.screens.appliance_costs

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable

private const val ROUTE = "ApplianceCosts"
internal const val APPLIANCE_IDX = "applianceIdx"
internal const val APPLIANCE_NAME = "name"

fun NavGraphBuilder.applianceCostsScreen(
    setTitle: (String) -> Unit,
) {

    composable(
        route = "$ROUTE/{$APPLIANCE_IDX}?$APPLIANCE_NAME={$APPLIANCE_NAME}",
    ) {

        val viewModel: ApplianceCostsViewModel = hiltViewModel()
        val state by viewModel.uiState.collectAsStateWithLifecycle()

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
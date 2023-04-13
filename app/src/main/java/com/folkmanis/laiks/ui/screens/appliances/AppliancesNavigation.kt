package com.folkmanis.laiks.ui.screens.appliances

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.folkmanis.laiks.LaiksAppState

private const val ROUTE = "Appliances"

fun NavGraphBuilder.appliancesScreen(
    appState: LaiksAppState,
    onEditAppliance: (id: String) -> Unit = {},
    onAddAppliance: () -> Unit = {},
) {


    composable(ROUTE) {

   val viewModel: AppliancesViewModel = hiltViewModel()
        val appliances by viewModel.appliances.collectAsStateWithLifecycle(initialValue = emptyList())

        AppliancesScreen(
            appliances = appliances,
            onEdit = onEditAppliance,
            onAdd = onAddAppliance,
            onDelete = viewModel::delete,
            onNavigateBack = appState::navigateToDefault,
        )
    }
}

fun NavController.navigateToAppliances(navOptions: NavOptions? = null) {
    navigate(ROUTE, navOptions)
}
package com.folkmanis.laiks.ui.screens.appliances

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.folkmanis.laiks.R
import com.folkmanis.laiks.model.ApplianceType
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.model.PowerApplianceRecord
import com.folkmanis.laiks.utilities.composables.ErrorScreen
import com.folkmanis.laiks.utilities.composables.LoadingScreen

private const val ROUTE = "Appliances"

fun NavGraphBuilder.appliancesScreen(
    onEditAppliance: (id: String) -> Unit = {},
    onAddAppliance: () -> Unit = {},
    onViewAppliance: (id: String) -> Unit = {},
    onSelectAppliance: (PowerApplianceRecord) -> Unit = {},
    setTitle: (String) -> Unit,
) {


    composable(ROUTE) {

        val viewModel: AppliancesViewModel = hiltViewModel()

        val state = viewModel.uiState.collectAsStateWithLifecycle().value

        val title = stringResource(id = R.string.appliances_screen)
        LaunchedEffect(Unit) {
            setTitle(title)
        }

        when (state) {
            is AppliancesUiState.Loading -> LoadingScreen()
            is AppliancesUiState.Error -> ErrorScreen(reason = state.reason)
            is AppliancesUiState.Success -> {
                val records = state.records
                AppliancesScreen(
                    records = records,
                    onEdit = { onEditAppliance(records[it].id) },
                    onAddExisting = viewModel::addAppliance,
                    onAddNew = onAddAppliance,
                    onDelete = viewModel::delete,
                    onSelectAppliance = {
                        onSelectAppliance(records[it])
                    },
                    onView = { onViewAppliance(records[it].id) }
                )

            }
        }

    }
}

fun NavController.navigateToAppliances(builder: (NavOptionsBuilder.() -> Unit) = {}) {
    navigate(ROUTE, builder)
}
package com.folkmanis.laiks.ui.screens.user_settings.appliances

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
import com.folkmanis.laiks.utilities.composables.ErrorScreen
import com.folkmanis.laiks.utilities.composables.LoadingScreen
import kotlinx.coroutines.flow.MutableStateFlow

private const val ROUTE = "Appliances"

fun NavGraphBuilder.appliancesScreen(
    onEditAppliance: (idx: Int) -> Unit = {},
    onSelectAppliance: (idx: Int) -> Unit = {},
    onAddAppliance: () -> Unit = {},
    setTitle: (String) -> Unit,
) {


    composable(ROUTE) {

        val viewModel: AppliancesViewModel = hiltViewModel()

        val state by viewModel.uiState.collectAsStateWithLifecycle()

        val title = stringResource(id = R.string.appliances_screen)
        LaunchedEffect(Unit) {
            setTitle(title)
        }

        if(state.loading) {
            LoadingScreen()
        } else {
            AppliancesScreen(
                onEdit = onEditAppliance,
                onDelete = viewModel::delete,
                onSelectAppliance = onSelectAppliance,
                onAdd = onAddAppliance,
                appliances = state.appliances,
                busy = state.saving,
            )
        }

    }
}

fun NavController.navigateToAppliances(builder: (NavOptionsBuilder.() -> Unit) = {}) {
    navigate(ROUTE, builder)
}
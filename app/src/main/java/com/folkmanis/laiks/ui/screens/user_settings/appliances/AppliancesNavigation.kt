package com.folkmanis.laiks.ui.screens.user_settings.appliances

import android.util.Log
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
import com.folkmanis.laiks.utilities.composables.LoadingScreen

private const val ROUTE = "Appliances"

private const val TAG = "AppliancesScreen"

fun NavGraphBuilder.appliancesScreen(
    onSelectAppliance: (idx: Int, name: String) -> Unit,
    onEditAppliance: (idx: Int) -> Unit,
    onAddAppliance: () -> Unit,
    setTitle: (String) -> Unit,
) {


    composable(ROUTE) {

        val viewModel: AppliancesViewModel = hiltViewModel()

        val state by viewModel.uiState.collectAsStateWithLifecycle()

        val title = stringResource(id = R.string.appliances_screen)
        LaunchedEffect(title, setTitle) {
            Log.d(TAG, title)
            setTitle(title)
        }

        if (state.loading) {
            LoadingScreen()
        } else {
            AppliancesScreen(
                onEdit = onEditAppliance,
                onDelete = viewModel::delete,
                onSelectAppliance = onSelectAppliance,
                onAdd = onAddAppliance,
                appliances = state.appliances,
                busy = state.saving,
                onReorder = viewModel::reorder,
                onReorderEnd = viewModel::saveAppliances,
            )
        }

    }
}

fun NavController.navigateToAppliances(builder: (NavOptionsBuilder.() -> Unit) = {}) {
    navigate(ROUTE, builder)
}
package com.folkmanis.laiks.ui.screens.appliances

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
import com.folkmanis.laiks.data.domain.PowerApplianceRecord
import com.folkmanis.laiks.model.ApplianceType
import com.folkmanis.laiks.model.PowerAppliance

private const val ROUTE = "Appliances"

fun NavGraphBuilder.appliancesScreen(
    onEditAppliance: (id: String) -> Unit = {},
    onAddAppliance: () -> Unit = {},
    onSelectAppliance: (PowerApplianceRecord) -> Unit = {},
    setTitle: (String) -> Unit,
) {


    composable(ROUTE) {

        val viewModel: AppliancesViewModel = hiltViewModel()
        val appliances by viewModel.appliances.collectAsStateWithLifecycle(initialValue = emptyList())
        val isAdmin by viewModel.isAdmin.collectAsStateWithLifecycle(initialValue = false)

        val title = stringResource(id = R.string.appliances_screen)
        LaunchedEffect(Unit) {
            setTitle(title)
        }

        AppliancesScreen(
            appliances = appliances,
            onEdit = onEditAppliance,
            onAdd = onAddAppliance,
            onDelete = viewModel::delete,
            onSelectAppliance = { appliance: PowerAppliance ->
                onSelectAppliance(
                    PowerApplianceRecord(appliance, ApplianceType.SYSTEM.type)
                )
            },
            isAdmin = isAdmin,
        )
    }
}

fun NavController.navigateToAppliances(builder: (NavOptionsBuilder.() -> Unit) = {}) {
    navigate(ROUTE, builder)
}
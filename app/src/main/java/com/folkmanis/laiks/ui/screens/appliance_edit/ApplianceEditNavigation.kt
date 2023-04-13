package com.folkmanis.laiks.ui.screens.appliance_edit

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.composable

private const val ROUTE = "ApplianceEditor"
private const val APPLIANCE_ID = "applianceId"
private const val NEW_APPLIANCE = "new"
private const val COPY = "copy"


fun NavGraphBuilder.applianceEditScreen(
    onCopy: (String) -> Unit,
    popUp: ()->Unit,
) {

    composable(
        route = "$ROUTE/{$APPLIANCE_ID}?$COPY={$COPY}",
        arguments = listOf(
            navArgument(APPLIANCE_ID) {
                type = NavType.StringType
            },
            navArgument(COPY) {
                type = NavType.BoolType
                defaultValue = false
            }
        ),
    ) { backStackEntry ->

        val applianceId =
            backStackEntry.arguments?.getString(com.folkmanis.laiks.APPLIANCE_ID) ?: ""
        val createCopy =
            backStackEntry.arguments?.getBoolean(com.folkmanis.laiks.COPY) ?: false

        val viewModel: ApplianceEditViewModel = hiltViewModel()

        val editAction = if (applianceId == NEW_APPLIANCE) {
            EditActions.CreateNew
        } else if (createCopy) {
            EditActions.Copy(applianceId)
        } else {
            EditActions.Edit(applianceId)
        }
        LaunchedEffect(applianceId) {
            viewModel.loadAppliance(editAction)
        }

        ApplianceEdit(
            onNavigateBack = popUp,
            onCopy = onCopy,
            viewModel = viewModel,
        )

    }
}

fun NavController.editAppliance(applianceId: String, navOptions: NavOptions? = null) {
    navigate(
        route = "$ROUTE/$applianceId",
        navOptions = navOptions
    )
}

fun NavController.copyAppliance(applianceId: String, navOptions: NavOptions? = null) {
    navigate(
        route = "$ROUTE/$applianceId?$COPY=true",
        navOptions = navOptions
    )
}

fun NavController.newAppliance(navOptions: NavOptions? = null) {
    navigate(
        route = "$ROUTE/$NEW_APPLIANCE",
        navOptions = navOptions,
    )
}
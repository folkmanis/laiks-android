package com.folkmanis.laiks.ui.screens.appliance_edit

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.folkmanis.laiks.LaiksAppState

private const val ROUTE = "ApplianceEditor"
private const val APPLIANCE_ID = "applianceId"
private const val NEW_APPLIANCE = "new"
private const val COPY = "copy"


fun NavGraphBuilder.applianceEditScreen(
    onCopy: (String, NavOptionsBuilder.() -> Unit) -> Unit,
    appState: LaiksAppState.Loaded,
) {

    composable(
        route = "$ROUTE/{$APPLIANCE_ID}",
        arguments = listOf(
            navArgument(APPLIANCE_ID) {
                type = NavType.StringType
            },
        ),
    ) { backStackEntry ->

        val applianceId =
            backStackEntry.arguments?.getString(APPLIANCE_ID) ?: ""

        ApplianceEdit(
            onNavigateBack = appState::popUp,
            onCopy = {
                onCopy(it) {
                    launchSingleTop = true
                    popUpTo(ROUTE) { inclusive = true }
                }
            },
            editAction = EditActions.Edit(applianceId)
        )

    }
}

fun NavGraphBuilder.applianceCopyRoute(
    appState: LaiksAppState.Loaded,
) {
    composable(
        route = "$ROUTE/{$APPLIANCE_ID}/$COPY",
        arguments = listOf(
            navArgument(APPLIANCE_ID) { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        val applianceId =
            backStackEntry.arguments?.getString(APPLIANCE_ID) ?: ""

        ApplianceEdit(
            onNavigateBack = appState::popUp,
            editAction = EditActions.Copy(applianceId),
            onCopy = {},
        )

    }
}

fun NavGraphBuilder.applianceNewRoute(
    appState: LaiksAppState.Loaded,
) {
    composable(
        route = "$ROUTE/$NEW_APPLIANCE"
    ) {
        ApplianceEdit(
            onNavigateBack = appState::popUp,
            editAction = EditActions.CreateNew,
            onCopy = {},
        )
    }
}

fun NavController.editAppliance(applianceId: String, builder: (NavOptionsBuilder.() -> Unit) = {}) {
    navigate(
        route = "$ROUTE/$applianceId",
        builder
    )
}

fun NavController.copyAppliance(applianceId: String, builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(
        route = "$ROUTE/$applianceId/$COPY",
        builder = builder
    )
}

fun NavController.newAppliance(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(
        route = "$ROUTE/$NEW_APPLIANCE",
        builder = builder,
    )
}
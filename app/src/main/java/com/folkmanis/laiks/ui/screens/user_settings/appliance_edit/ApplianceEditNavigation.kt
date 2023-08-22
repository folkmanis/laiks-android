package com.folkmanis.laiks.ui.screens.user_settings.appliance_edit

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

private const val ROUTE = "ApplianceEditor"
private const val APPLIANCE_IDX = "applianceIdx"
private const val NEW_APPLIANCE = "new"


fun NavGraphBuilder.applianceEditScreen(
    setTitle: (String) -> Unit,
    onNavigateBack: () -> Unit,
) {

    composable(
        route = "$ROUTE/{$APPLIANCE_IDX}",
        arguments = listOf(
            navArgument(APPLIANCE_IDX) {
                type = NavType.IntType
            },
        ),
    ) { backStackEntry ->

        val applianceId =
            backStackEntry.arguments?.getInt(APPLIANCE_IDX)

        ApplianceEditScreen(
            onNavigateBack = onNavigateBack,
            idx = applianceId,
            setTitle = setTitle,
        )

    }
}

fun NavGraphBuilder.applianceNewRoute(
    onNavigateBack: () -> Unit,
    setTitle: (String) -> Unit,
) {
    composable(
        route = "$ROUTE/$NEW_APPLIANCE"
    ) {

        ApplianceEditScreen(
            onNavigateBack = onNavigateBack,
            idx = null,
            setTitle = setTitle,
        )
    }
}

fun NavController.editAppliance(idx: Int, builder: (NavOptionsBuilder.() -> Unit) = {}) {
    navigate(
        route = "$ROUTE/$idx",
        builder
    )
}

fun NavController.newAppliance(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(
        route = "$ROUTE/$NEW_APPLIANCE",
        builder = builder,
    )
}

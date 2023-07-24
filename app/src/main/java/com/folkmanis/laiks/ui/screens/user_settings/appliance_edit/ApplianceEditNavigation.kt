package com.folkmanis.laiks.ui.screens.user_settings.appliance_edit

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.folkmanis.laiks.R

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

        val title = stringResource(id = R.string.appliance_screen)
        LaunchedEffect(Unit) {
            setTitle(title)
        }

        ApplianceEditScreen(
            onNavigateBack = onNavigateBack,
            idx = applianceId,
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

        val title = stringResource(id = R.string.appliance_screen)
        LaunchedEffect(Unit) {
            setTitle(title)
        }

        ApplianceEditScreen(
            onNavigateBack = onNavigateBack,
            idx = null,
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

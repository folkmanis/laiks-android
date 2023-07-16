package com.folkmanis.laiks.ui.screens.appliance_edit

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
private const val APPLIANCE_ID = "applianceId"
private const val NEW_APPLIANCE = "new"
private const val COPY = "copy"


fun NavGraphBuilder.applianceEditScreen(
    onCopy: (String, NavOptionsBuilder.() -> Unit) -> Unit,
    setTitle: (String) -> Unit,
    onNavigateBack: () -> Unit,
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

        val title = stringResource(id = R.string.appliance_screen)
        LaunchedEffect(Unit) {
            setTitle(title)
        }

        ApplianceEdit(
            onNavigateBack = onNavigateBack,
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
    onNavigateBack: () -> Unit,
    setTitle: (String) -> Unit,
) {
    composable(
        route = "$ROUTE/{$APPLIANCE_ID}/$COPY",
        arguments = listOf(
            navArgument(APPLIANCE_ID) { type = NavType.StringType },
        ),
    ) { backStackEntry ->

        val title = stringResource(id = R.string.appliance_screen)
        LaunchedEffect(Unit) {
            setTitle(title)
        }

        val applianceId =
            backStackEntry.arguments?.getString(APPLIANCE_ID) ?: ""

        ApplianceEdit(
            onNavigateBack = onNavigateBack,
            editAction = EditActions.Copy(applianceId),
            onCopy = {},
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

        ApplianceEdit(
            onNavigateBack = onNavigateBack,
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

fun NavController.viewAppliance(applianceId: String, builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(
        route = "$ROUTE/$applianceId",
        builder = builder,
    )
}
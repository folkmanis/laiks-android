package com.folkmanis.laiks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.folkmanis.laiks.ui.screens.appliance_edit.*
import com.folkmanis.laiks.ui.screens.appliances.appliancesScreen
import com.folkmanis.laiks.ui.screens.clock.CLOCK_ROUTE
import com.folkmanis.laiks.ui.screens.clock.clockScreen
import com.folkmanis.laiks.ui.screens.prices.navigateToPrices
import com.folkmanis.laiks.ui.screens.prices.pricesScreen
import com.folkmanis.laiks.ui.screens.user_edit.editUser
import com.folkmanis.laiks.ui.screens.user_edit.userEditScreen
import com.folkmanis.laiks.ui.screens.users.usersScreen
import kotlinx.coroutines.CoroutineScope


@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) =
    remember(navController, coroutineScope) {
        LaiksAppState(navController, coroutineScope)
    }

@Composable
fun LaiksAppScreen(
    modifier: Modifier = Modifier,
) {

    val appState = rememberAppState()

    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = CLOCK_ROUTE,
        modifier = modifier
    ) {

        clockScreen(
            appState = appState,
            onNavigateToPrices = navController::navigateToPrices
        )

        pricesScreen(appState)

        usersScreen(appState, navController::editUser)

        userEditScreen(appState)

        appliancesScreen(
            appState = appState,
            onEditAppliance = navController::editAppliance,
            onAddAppliance = navController::newAppliance
        )

        applianceEditScreen(
            appState = appState,
            onCopy = navController::copyAppliance,
        )

        applianceNewRoute(appState)

        applianceCopyRoute(appState)
    }
}


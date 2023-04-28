package com.folkmanis.laiks

import android.content.res.Resources
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.folkmanis.laiks.ui.screens.appliance_costs.applianceCostsScreen
import com.folkmanis.laiks.ui.screens.appliance_costs.navigateToApplianceCosts
import com.folkmanis.laiks.ui.screens.appliance_edit.applianceCopyRoute
import com.folkmanis.laiks.ui.screens.appliance_edit.applianceEditScreen
import com.folkmanis.laiks.ui.screens.appliance_edit.applianceNewRoute
import com.folkmanis.laiks.ui.screens.appliance_edit.copyAppliance
import com.folkmanis.laiks.ui.screens.appliance_edit.editAppliance
import com.folkmanis.laiks.ui.screens.appliance_edit.newAppliance
import com.folkmanis.laiks.ui.screens.appliances.appliancesScreen
import com.folkmanis.laiks.ui.screens.clock.CLOCK_ROUTE
import com.folkmanis.laiks.ui.screens.clock.clockScreen
import com.folkmanis.laiks.ui.screens.prices.navigateToPrices
import com.folkmanis.laiks.ui.screens.prices.pricesScreen
import com.folkmanis.laiks.ui.screens.user_edit.editUser
import com.folkmanis.laiks.ui.screens.user_edit.userEditScreen
import com.folkmanis.laiks.ui.screens.users.usersScreen
import com.folkmanis.laiks.utilities.composables.LoadingScreen
import com.folkmanis.laiks.utilities.snackbar.SnackbarManager
import kotlinx.coroutines.CoroutineScope


@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

@Composable
fun LaiksAppScreen(
    windowSize: WindowSizeClass,
    modifier: Modifier = Modifier,
) {

    val viewModel: LaiksAppViewModel = hiltViewModel()
    val appState = viewModel.appState.collectAsStateWithLifecycle().value

    val navController = rememberNavController()
    val resources = resources()

    LaunchedEffect(windowSize) {
        viewModel.setState(
            windowSize = windowSize,
            navController = navController,
            resources = resources,
        )
    }

    when(appState) {
        is LaiksAppState.Loading -> LoadingScreen()
        is LaiksAppState.Loaded -> {
            NavHost(
                navController = navController,
                startDestination = CLOCK_ROUTE,
                modifier = modifier
            ) {

                clockScreen(
                    appState = appState,
                    onNavigateToPrices = navController::navigateToPrices,
                    onNavigateToAppliance = navController::navigateToApplianceCosts,
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

                applianceCostsScreen(appState)

            }

        }
    }

}


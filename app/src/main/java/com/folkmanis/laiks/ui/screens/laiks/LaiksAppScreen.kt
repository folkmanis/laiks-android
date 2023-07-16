package com.folkmanis.laiks.ui.screens.laiks

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.folkmanis.laiks.ui.screens.appliance_costs.applianceCostsScreen
import com.folkmanis.laiks.ui.screens.appliance_costs.applianceCosts
import com.folkmanis.laiks.ui.screens.appliance_edit.applianceCopyRoute
import com.folkmanis.laiks.ui.screens.appliance_edit.applianceEditScreen
import com.folkmanis.laiks.ui.screens.appliance_edit.applianceNewRoute
import com.folkmanis.laiks.ui.screens.appliance_edit.copyAppliance
import com.folkmanis.laiks.ui.screens.appliance_edit.editAppliance
import com.folkmanis.laiks.ui.screens.appliance_edit.newAppliance
import com.folkmanis.laiks.ui.screens.appliance_edit.viewAppliance
import com.folkmanis.laiks.ui.screens.appliances.appliancesScreen
import com.folkmanis.laiks.ui.screens.appliances.navigateToAppliances
import com.folkmanis.laiks.ui.screens.clock.CLOCK_ROUTE
import com.folkmanis.laiks.ui.screens.clock.clockScreen
import com.folkmanis.laiks.ui.screens.clock.navigateToClock
import com.folkmanis.laiks.ui.screens.prices.navigateToPrices
import com.folkmanis.laiks.ui.screens.prices.pricesScreen
import com.folkmanis.laiks.ui.screens.user_edit.editUser
import com.folkmanis.laiks.ui.screens.user_edit.userEditScreen
import com.folkmanis.laiks.ui.screens.user_menu.UserMenu
import com.folkmanis.laiks.ui.screens.user_settings.userSettings
import com.folkmanis.laiks.ui.screens.user_settings.userSettingsScreen
import com.folkmanis.laiks.ui.screens.users.navigateToUsers
import com.folkmanis.laiks.ui.screens.users.usersScreen


@Composable
fun LaiksAppScreen(
    windowSize: WindowSizeClass,
    modifier: Modifier = Modifier,
) {

    val viewModel: LaiksAppViewModel = hiltViewModel()
    val appState = viewModel.appState.collectAsStateWithLifecycle().value

    val navController = rememberNavController()
    val resources = LocalContext.current.resources

    LaunchedEffect(Unit) {
        viewModel.initialize(
            resources = resources,
        )
    }

    LaiksAppScaffold(
        title = appState.title,
        canNavigateBack = navController.previousBackStackEntry != null,
        snackbarHostState = viewModel.snackbarHostState,
        appMenu = {
            UserMenu(
                onUsersAdmin = {
                    navController.navigateToUsers { popUpTo(CLOCK_ROUTE) }
                },
                onAppliancesAdmin = {
                    navController.navigateToAppliances { popUpTo(CLOCK_ROUTE) }
                },
                onPopToStart = {
                    navController.navigateToClock {
                        launchSingleTop = true
                        popUpTo(0) { inclusive = true }
                    }
                },
                onUserSettings = {
                    navController.userSettings(
                        userId = requireNotNull(appState.user?.id),
                        name = appState.user?.name
                    ) {
                        popUpTo(CLOCK_ROUTE)
                    }
                },
                user = appState.user,
            )
        },
        onNavigateBack = navController::popBackStack,
        modifier = modifier
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = CLOCK_ROUTE,
            modifier = Modifier.padding(innerPadding)
        ) {

            clockScreen(
                onNavigateToPrices = navController::navigateToPrices,
                onNavigateToAppliance = navController::applianceCosts,
                setTitle = viewModel::setTitle,
                windowHeight = windowSize.heightSizeClass,
            )

            pricesScreen(
                setTitle = viewModel::setTitle,
            )

            usersScreen(
                setTitle = viewModel::setTitle,
                onEditUser = navController::editUser
            )

            userEditScreen(
                setTitle = viewModel::setTitle,
            )

            appliancesScreen(
                onEditAppliance = navController::editAppliance,
                onAddAppliance = navController::newAppliance,
                setTitle = viewModel::setTitle,
                onSelectAppliance = navController::applianceCosts,
                onViewAppliance = navController::viewAppliance
            )

            applianceEditScreen(
                onCopy = navController::copyAppliance,
                setTitle = viewModel::setTitle,
                onNavigateBack = navController::popBackStack,
            )

            applianceNewRoute(
                onNavigateBack = navController::popBackStack,
                setTitle = viewModel::setTitle,
            )

            applianceCopyRoute(
                onNavigateBack = navController::popBackStack,
                setTitle = viewModel::setTitle,
            )

            applianceCostsScreen(
                setTitle = viewModel::setTitle,
            )

            userSettingsScreen(
                setTitle = viewModel::setTitle,
                onEditAppliances = navController::navigateToAppliances,
            )

        }
    }

}


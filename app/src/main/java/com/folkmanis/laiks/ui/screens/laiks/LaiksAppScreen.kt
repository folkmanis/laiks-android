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
import com.folkmanis.laiks.ui.screens.appliance_costs.applianceCosts
import com.folkmanis.laiks.ui.screens.appliance_costs.applianceCostsScreen
import com.folkmanis.laiks.ui.screens.clock.CLOCK_ROUTE
import com.folkmanis.laiks.ui.screens.clock.clockScreen
import com.folkmanis.laiks.ui.screens.clock.navigateToClockSingleTop
import com.folkmanis.laiks.ui.screens.login.loginGraph
import com.folkmanis.laiks.ui.screens.login.navigateToLogin
import com.folkmanis.laiks.ui.screens.prices.navigateToPrices
import com.folkmanis.laiks.ui.screens.prices.pricesScreen
import com.folkmanis.laiks.ui.screens.user_menu.UserMenu
import com.folkmanis.laiks.ui.screens.user_settings.appliances.navigateToAppliances
import com.folkmanis.laiks.ui.screens.user_settings.userSettings
import com.folkmanis.laiks.ui.screens.user_settings.userSettingsGraph


@Composable
fun LaiksAppScreen(
    windowSize: WindowSizeClass,
    modifier: Modifier = Modifier,
) {

    val context = LocalContext.current

    val viewModel: LaiksAppViewModel = hiltViewModel()
    val appState = viewModel.appState.collectAsStateWithLifecycle().value

    val navController = rememberNavController()
    val resources = LocalContext.current.resources

    LaunchedEffect(resources) {
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
                onUserSettings = {
                    appState.laiksUser?.also {
                        viewModel.setTitle(it.name)
                    }
                    navController.userSettings {
                        popUpTo(CLOCK_ROUTE)
                    }
                },
                onEditAppliances = {
                    navController.navigateToAppliances {
                        popUpTo(CLOCK_ROUTE)
                    }
                },
                onLogin = navController::navigateToLogin,
                onLogout = {
                    viewModel.logOut(context)
                    navController.navigateToClockSingleTop()
                },
                isAnonymous = appState.user?.isAnonymous ?: false,
                onSetVatEnabled = viewModel::setVatEnabled,
                isVatEnabled = appState.laiksUser?.includeVat ?: false,
                npBlocked = appState.npBlocked,
                photoUrl = appState.user?.photoUrl,
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
                windowWidth = windowSize.widthSizeClass,
            )

            pricesScreen(
                setTitle = viewModel::setTitle,
                onSetMarketZone = {
                    navController.userSettings {
                        popUpTo(CLOCK_ROUTE)
                    }
                }
            )

            applianceCostsScreen(
                setTitle = viewModel::setTitle,
                onSetMarketZone = {
                    navController.userSettings {
                        popUpTo(CLOCK_ROUTE)
                    }
                },
            )

            userSettingsGraph(
                navController = navController,
                setTitle = viewModel::setTitle,
                appState = appState,
            )

            loginGraph(
                navController = navController,
                setTitle = viewModel::setTitle,
                windowWidth = windowSize.widthSizeClass,
            )

        }
    }

}


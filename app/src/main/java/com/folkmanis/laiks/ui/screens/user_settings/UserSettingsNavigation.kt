package com.folkmanis.laiks.ui.screens.user_settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navigation
import com.folkmanis.laiks.ui.screens.appliance_costs.applianceCosts
import com.folkmanis.laiks.ui.screens.clock.navigateToClockSingleTop
import com.folkmanis.laiks.ui.screens.user_settings.appliance_edit.applianceEditScreen
import com.folkmanis.laiks.ui.screens.user_settings.appliance_edit.applianceNewRoute
import com.folkmanis.laiks.ui.screens.user_settings.appliance_edit.editAppliance
import com.folkmanis.laiks.ui.screens.user_settings.appliance_edit.newAppliance
import com.folkmanis.laiks.ui.screens.user_settings.appliances.appliancesScreen
import com.folkmanis.laiks.ui.screens.user_settings.appliances.navigateToAppliances
import com.folkmanis.laiks.ui.screens.user_settings.main_settings.NEXT_ROUTE
import com.folkmanis.laiks.ui.screens.user_settings.main_settings.SHOULD_SET_ZONE
import com.folkmanis.laiks.ui.screens.user_settings.main_settings.mainSettingsScreen
import com.folkmanis.laiks.ui.screens.user_settings.main_settings.ROUTE as MAIN_SETTINGS_ROUTE

const val ROUTE = "UserSettings"

fun NavGraphBuilder.userSettingsGraph(
    navController: NavController,
    setTitle: (String) -> Unit,
) {

    navigation(
        startDestination = MAIN_SETTINGS_ROUTE,
        route = ROUTE,
    ) {

        mainSettingsScreen(
            setTitle = setTitle,
            onUserAppliances = navController::navigateToAppliances,
            onUserDeleted = navController::navigateToClockSingleTop,
            onMarketZoneSet = { route -> navController.navigate(route) },
            onMarketZoneNotSet = navController::navigateToClockSingleTop,
        )

        appliancesScreen(
            onEditAppliance = navController::editAppliance,
            onAddAppliance = navController::newAppliance,
            setTitle = setTitle,
            onSelectAppliance = navController::applianceCosts,
        )

        applianceEditScreen(
            setTitle = setTitle,
            onNavigateBack = navController::popBackStack,
        )

        applianceNewRoute(
            onNavigateBack = navController::popBackStack,
            setTitle = setTitle,
        )


    }
}

fun NavController.userSettings(
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(ROUTE, builder)
}

fun NavController.setMarketZone(
    nextRoute: String,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(
        "$ROUTE?$SHOULD_SET_ZONE=true&$NEXT_ROUTE=$nextRoute",
        builder
    )
}
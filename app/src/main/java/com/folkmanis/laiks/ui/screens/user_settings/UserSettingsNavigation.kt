package com.folkmanis.laiks.ui.screens.user_settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navigation
import com.folkmanis.laiks.ui.screens.appliance_costs.applianceCosts
import com.folkmanis.laiks.ui.screens.user_settings.appliance_edit.applianceCopyRoute
import com.folkmanis.laiks.ui.screens.user_settings.appliance_edit.applianceEditScreen
import com.folkmanis.laiks.ui.screens.user_settings.appliance_edit.applianceNewRoute
import com.folkmanis.laiks.ui.screens.user_settings.appliance_edit.copyAppliance
import com.folkmanis.laiks.ui.screens.user_settings.appliance_edit.editAppliance
import com.folkmanis.laiks.ui.screens.user_settings.appliance_edit.newAppliance
import com.folkmanis.laiks.ui.screens.user_settings.appliance_edit.viewAppliance
import com.folkmanis.laiks.ui.screens.user_settings.appliance_select.applianceSelectScreen
import com.folkmanis.laiks.ui.screens.user_settings.appliance_select.navigateToAddAppliance
import com.folkmanis.laiks.ui.screens.user_settings.appliances.appliancesScreen
import com.folkmanis.laiks.ui.screens.user_settings.appliances.navigateToAppliances
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
            onEditAppliances = navController::navigateToAppliances,
        )

        appliancesScreen(
            onEditAppliance = navController::editAppliance,
            onAddAppliance = navController::navigateToAddAppliance,
            setTitle = setTitle,
            onSelectAppliance = navController::applianceCosts,
        )

        applianceSelectScreen(
            setTitle = setTitle,
            onApplianceAdded = {navController.popBackStack()},
            onCopyAppliance = { TODO() },
            onNewAppliance = { TODO() },
        )

        applianceEditScreen(
            onCopy = navController::copyAppliance,
            setTitle = setTitle,
            onNavigateBack = navController::popBackStack,
        )

        applianceNewRoute(
            onNavigateBack = navController::popBackStack,
            setTitle = setTitle,
        )

        applianceCopyRoute(
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

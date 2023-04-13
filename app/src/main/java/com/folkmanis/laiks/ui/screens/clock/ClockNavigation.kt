package com.folkmanis.laiks.ui.screens.clock

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.folkmanis.laiks.LaiksAppState
import com.folkmanis.laiks.LaiksScreen
import com.folkmanis.laiks.ui.screens.user_menu.UserMenu

fun NavGraphBuilder.clockScreen(
    appState: LaiksAppState,
    onNavigateToPrices: () -> Unit,
) {

    composable("Clock") {

        val viewModel: ClockViewModel = hiltViewModel()
        val uiState by viewModel
            .uiState
            .collectAsStateWithLifecycle()
        val pricesAllowed by viewModel.isPricesAllowed.collectAsStateWithLifecycle(initialValue = false)

        ClockScreen(
            uiState = uiState,
            pricesAllowed = pricesAllowed,
            onShowPrices = onNavigateToPrices,
            updateOffset = viewModel::updateOffset,
            actions = { appState.AppUserMenu() }
//            {
//                UserMenu(
//                    onUsersAdmin = appState::navigateToUsersAdmin,
//                    onAppliancesAdmin = appState::navigateToAppliancesAdmin,
//                    onLogout = appState::navigateToDefault
//                )
//            }
        )

    }

}

fun NavController.navigateToClock(navOptions: NavOptions? = null) {
    this.navigate(LaiksScreen.Clock.route, navOptions)
}

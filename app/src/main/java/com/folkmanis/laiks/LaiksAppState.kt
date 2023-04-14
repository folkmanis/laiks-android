package com.folkmanis.laiks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import com.folkmanis.laiks.ui.screens.appliances.navigateToAppliances
import com.folkmanis.laiks.ui.screens.clock.CLOCK_ROUTE
import com.folkmanis.laiks.ui.screens.clock.navigateToClock
import com.folkmanis.laiks.ui.screens.user_menu.UserMenu
import com.folkmanis.laiks.ui.screens.users.navigateToUsers
import kotlinx.coroutines.CoroutineScope

@Stable
class LaiksAppState(
    val navController: NavHostController,
    coroutineScope: CoroutineScope,
) {

    @Composable
    fun AppUserMenu() {
        UserMenu(
            onUsersAdmin = ::navigateToUsersAdmin,
            onAppliancesAdmin = ::navigateToAppliancesAdmin,
            onLogout = ::navigateToDefault
        )
    }

    fun popUp() {
        navController.popBackStack()
    }

    private fun navigateToUsersAdmin() {
        navController.navigateToUsers {
            popUpTo(CLOCK_ROUTE)
        }
    }

    private fun navigateToAppliancesAdmin() {
        navController.navigateToAppliances {
            popUpTo(CLOCK_ROUTE)
        }
    }

    private fun navigateToDefault() {
        navController.navigateToClock {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }
}
package com.folkmanis.laiks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.folkmanis.laiks.ui.screens.user_menu.UserMenu
import kotlinx.coroutines.CoroutineScope

@Stable
class LaiksAppState(
    val navController: NavHostController,
    coroutineScope: CoroutineScope,
) {

    @Composable
    fun AppUserMenu()  {
       UserMenu(
            onUsersAdmin = ::navigateToUsersAdmin,
            onAppliancesAdmin = ::navigateToAppliancesAdmin,
            onLogout = ::navigateToDefault
        )
    }


    val canNavigateBack: Boolean
        get() = navController.previousBackStackEntry != null

    fun popUp() {
        navController.popBackStack()
    }

    fun navigate(route: String) {
        navController.navigate(route) { launchSingleTop = true }
    }

    fun navigateAndPop(route: String, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp)
        }
    }

    fun navigateToUsersAdmin() {
        navController.navigate(LaiksScreen.Users.route) {
            popUpTo(LaiksScreen.defaultScreen.route)
        }
    }

    fun navigateToAppliancesAdmin() {
        navController.navigate(LaiksScreen.Appliances.route) {
            popUpTo(LaiksScreen.defaultScreen.route)
        }
    }

    fun navigateToDefault() {
        navController.popBackStack(LaiksScreen.Clock.route, false)
    }
}
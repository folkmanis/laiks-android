package com.folkmanis.laiks

import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope

@Stable
class LaiksAppState(
    val navController: NavHostController,
    coroutineScope: CoroutineScope,
) {

    val canNavigateBack: Boolean
        get() = navController.previousBackStackEntry != null

    fun popUp() {
        navController.popBackStack()
    }

    fun navigate(route: String) {
        navController.navigate(route) { launchSingleTop = true }
    }

    fun clearAndNavigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
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
}
package com.folkmanis.laiks

import android.content.res.Resources
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import com.folkmanis.laiks.model.LaiksUser
import com.folkmanis.laiks.ui.screens.appliances.navigateToAppliances
import com.folkmanis.laiks.ui.screens.clock.CLOCK_ROUTE
import com.folkmanis.laiks.ui.screens.clock.navigateToClock
import com.folkmanis.laiks.ui.screens.user_menu.UserMenu
import com.folkmanis.laiks.ui.screens.users.navigateToUsers
import com.folkmanis.laiks.utilities.snackbar.SnackbarManager
import com.folkmanis.laiks.utilities.snackbar.SnackbarMessage.Companion.toMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

//@Stable
data class LaiksAppState(
    val windowSize: WindowSizeClass,
    val navController: NavHostController,
    val user: LaiksUser?,
    coroutineScope: CoroutineScope,
    val snackbarHostState: SnackbarHostState,
    private val snackbarManager: SnackbarManager,
    private val resources: Resources,
) {

    init {
        coroutineScope.launch {
            snackbarManager
                .snackbarMessages
                .filterNotNull()
                .collect { snackbarMessage ->
                    val text = snackbarMessage.toMessage(resources)
                    snackbarHostState.showSnackbar(text)
                }
        }
    }

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
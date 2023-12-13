package com.folkmanis.laiks.ui.screens.login

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navigation
import com.folkmanis.laiks.ui.screens.clock.CLOCK_ROUTE
import com.folkmanis.laiks.ui.screens.clock.navigateToClockSingleTop
import com.folkmanis.laiks.ui.screens.login.login_email.loginEmailNavigation
import com.folkmanis.laiks.ui.screens.login.login_email.navigateToLoginEmailScreen
import com.folkmanis.laiks.ui.screens.login.login_select.LOGIN_SELECT_ROUTE
import com.folkmanis.laiks.ui.screens.login.login_select.loginSelectScreen
import com.folkmanis.laiks.ui.screens.login.password_reset.navigateToPasswordReset
import com.folkmanis.laiks.ui.screens.login.password_reset.passwordResetNavigation
import com.folkmanis.laiks.ui.screens.login.user_registration.navigateToNewUserRegistration
import com.folkmanis.laiks.ui.screens.login.user_registration.userRegistration
import com.folkmanis.laiks.ui.screens.user_settings.userSettings

const val LOGIN_ROUTE = "Login"

fun NavGraphBuilder.loginGraph(
    navController: NavController,
    setTitle: (String) -> Unit,
    windowWidth: WindowWidthSizeClass,
) {

    navigation(
        startDestination = LOGIN_SELECT_ROUTE,
        route = LOGIN_ROUTE,
    ) {

        loginSelectScreen(
            setTitle = setTitle,
            windowWidth = windowWidth,
            onLoginWithEmail = navController::navigateToLoginEmailScreen,
            onRegisterWithEmail = navController::navigateToNewUserRegistration,
            onLogin = navController::navigateToClockSingleTop,
            onLaiksUserCreated = {
                navController.userSettings {
                    launchSingleTop = true
                    popUpTo(CLOCK_ROUTE)
                }
            },
        )

        passwordResetNavigation(
            setTitle = setTitle,
            onReset = navController::navigateToLoginEmailScreen,
        )

        loginEmailNavigation(
            setTitle=setTitle,
            windowWidth = windowWidth,
            onLogin = navController::navigateToClockSingleTop,
            onResetPassword = navController::navigateToPasswordReset,
        )

        userRegistration(
            setTitle = setTitle,
            onUserRegistered = {
                navController.userSettings {
                    popUpTo(CLOCK_ROUTE)
                }
            }
        )


    }

}


fun NavController.navigateToLogin(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(LOGIN_ROUTE, builder)
}
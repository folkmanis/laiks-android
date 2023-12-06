package com.folkmanis.laiks.ui.screens.login.login_email

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.folkmanis.laiks.R

private const val LOGIN_EMAIL_ROUTE = "LoginWithEmail"
fun NavGraphBuilder.loginEmailNavigation(
    setTitle: (String) -> Unit,
    windowWidth: WindowWidthSizeClass,
    onLogin: () -> Unit,
    onResetPassword: ()->Unit,
) {
    composable(LOGIN_EMAIL_ROUTE) {

        val viewModel: LoginEmailViewModel = hiltViewModel()

        val uiState = viewModel.uiState

        val title = stringResource(R.string.login_title)
        LaunchedEffect(title, setTitle) {
            setTitle(title)
        }

        val isHorizontal = windowWidth != WindowWidthSizeClass.Compact

        LoginEmailScreen(
            email = uiState.email,
            password = uiState.password,
            busy = uiState.isBusy,
            onSetEmail = viewModel::setEmail,
            onSetPassword = viewModel::setPassword,
            onEmailLogin = { viewModel.loginWithEmail(afterLogin = onLogin) },
            onPasswordReset = onResetPassword,
            isHorizontal = isHorizontal,
        )

    }
}

fun NavController.navigateToLoginEmailScreen(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(LOGIN_EMAIL_ROUTE, builder)
}
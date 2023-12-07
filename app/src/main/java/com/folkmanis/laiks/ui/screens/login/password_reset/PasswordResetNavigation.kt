package com.folkmanis.laiks.ui.screens.login.password_reset

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.folkmanis.laiks.R

private const val PASSWORD_RESET_ROUTE = "PasswordReset"

fun NavGraphBuilder.passwordResetNavigation(
    setTitle: (String) -> Unit,
    onReset: () -> Unit,
) {
    composable(PASSWORD_RESET_ROUTE) {

        val viewModel: PasswordResetViewModel = hiltViewModel()

        val uiState = viewModel.uiState

        val title = stringResource(R.string.password_reset_screen_title)
        LaunchedEffect(title) {
            setTitle(title)
        }

        PasswordResetScreen(
            onReset = {
                viewModel.resetPassword(onReset)
            },
            email = uiState.email,
            setEmail = viewModel::setEmail,
            enabled = !uiState.isBusy,
        )

    }

}

fun NavController.navigateToPasswordReset(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(PASSWORD_RESET_ROUTE, builder)
}
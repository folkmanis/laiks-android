package com.folkmanis.laiks.ui.screens.password_reset

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.folkmanis.laiks.R

private const val PASSWORD_RESET_ROUTE = "PasswordReset"

fun NavGraphBuilder.passwordReset(
    setTitle: (String) -> Unit,
    onPasswordRequestSent: () -> Unit,
) {
    composable(PASSWORD_RESET_ROUTE) {
        val viewModel: PasswordResetViewModel = hiltViewModel()

        val uiState by viewModel.state

        val title = stringResource(R.string.password_reset_screen_title)
        LaunchedEffect(title) {
            setTitle(title)
        }

        PasswordResetScreen(
            onReset = { viewModel.resetPassword(onPasswordRequestSent) },
            email = uiState.email,
            setEmail = viewModel::setEmail
        )
    }
}

fun NavController.navigateToPasswordReset(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(PASSWORD_RESET_ROUTE, builder)
}
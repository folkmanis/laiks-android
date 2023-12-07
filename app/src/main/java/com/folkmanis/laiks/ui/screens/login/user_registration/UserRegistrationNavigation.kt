package com.folkmanis.laiks.ui.screens.login.user_registration

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.folkmanis.laiks.R

private const val USER_REGISTRATION_ROUTE = "UserRegistration"

fun NavGraphBuilder.userRegistration(
    setTitle: (String) -> Unit,
    onUserRegistered: () -> Unit
) {

    composable(USER_REGISTRATION_ROUTE) {

        val viewModel: UserRegistrationViewModel = hiltViewModel()
        val uiState by viewModel.uiState

        val title = stringResource(R.string.login_title)
        LaunchedEffect(title) {
            setTitle(title)
        }

        UserRegistrationScreen(
            uiState = uiState,
            setEmail = viewModel::setEmail,
            setPassword = viewModel::setPassword,
            setPasswordRepeat = viewModel::setPasswordRepeat,
            setDisplayName = viewModel::setDisplayName,
            onRegisterUser = {
                viewModel.createUser {user->
                    user.displayName?.also(setTitle)
                    onUserRegistered()
                }
            },
        )
    }

}

fun NavController.navigateToNewUserRegistration(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(USER_REGISTRATION_ROUTE, builder)
}
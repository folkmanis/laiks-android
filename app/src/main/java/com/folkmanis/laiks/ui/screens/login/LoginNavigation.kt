package com.folkmanis.laiks.ui.screens.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.folkmanis.laiks.R
import com.folkmanis.laiks.utilities.oauth.getGoogleSignInIntent

const val LOGIN_ROUTE = "Login"

fun NavGraphBuilder.loginScreen(
    onLogin: () -> Unit,
    setTitle: (String) -> Unit,
) {
    composable(LOGIN_ROUTE) {
        val title = stringResource(R.string.login_title)
        LaunchedEffect(title, setTitle) {
            setTitle(title)
        }

        val viewModel: LoginViewModel = hiltViewModel()

        val uiState by viewModel.uiState

        val context = LocalContext.current

        LaunchedEffect(true) {
            viewModel.logout(context)
        }

        val loginLauncher = rememberLauncherForActivityResult(
            FirebaseAuthUIActivityResultContract()
        ) { result ->
            viewModel.onLoginResult(result, onLogin)
        }


        LoginScreen(
            email = uiState.email,
            password = uiState.password,
            onSetEmail = viewModel::setEmail,
            onSetPassword = viewModel::setPassword,
            onGoogleLogin = { loginLauncher.launch(getGoogleSignInIntent()) },
            onEmailLogin = { viewModel.loginWithEmail(onLogin) },
        )

    }
}


fun NavController.navigateToLogin(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(LOGIN_ROUTE, builder)
}
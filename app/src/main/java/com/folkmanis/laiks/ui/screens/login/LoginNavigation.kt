package com.folkmanis.laiks.ui.screens.login

import android.util.Log
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.folkmanis.laiks.R
import com.folkmanis.laiks.utilities.onetap.OneTapSignInWithGoogle
import com.folkmanis.laiks.utilities.onetap.rememberOneTapSignInState

const val LOGIN_ROUTE = "Login"

private const val TAG = "loginScreen"

fun NavGraphBuilder.loginScreen(
    onLogin: () -> Unit,
    onPasswordReset: () -> Unit,
    onLaiksUserCreated: () -> Unit,
    onRegisterWithEmail: () -> Unit,
    setTitle: (String) -> Unit,
    windowWidth: WindowWidthSizeClass,
) {
    composable(LOGIN_ROUTE) {
        val title = stringResource(R.string.login_title)
        LaunchedEffect(title, setTitle) {
            setTitle(title)
        }

        val viewModel: LoginViewModel = hiltViewModel()

        val uiState = viewModel.uiState

        val googleLoginState = rememberOneTapSignInState()

        val context = LocalContext.current

        val isHorizontal = windowWidth != WindowWidthSizeClass.Compact

        LaunchedEffect(true) {
            viewModel.logout(context)
        }

        LoginScreen(
            email = uiState.email,
            password = uiState.password,
            busy = uiState.isBusy,
            onSetEmail = viewModel::setEmail,
            onSetPassword = viewModel::setPassword,
            onGoogleLogin = { googleLoginState.open() },
            onEmailLogin = { viewModel.loginWithEmail(onLogin, onLaiksUserCreated) },
            onPasswordReset = onPasswordReset,
            isHorizontal = isHorizontal,
            onRegisterWithEmail = onRegisterWithEmail,
        )

        OneTapSignInWithGoogle(
            state = googleLoginState,
            clientId = stringResource(R.string.one_tap_web_client_id),
            onTokenIdReceived = {
                Log.d(TAG, "Token: $it")
                viewModel.loginWithGoogle(
                    tokenId = it,
                    afterLogin = onLogin,
                    onLaiksUserCreated = onLaiksUserCreated,
                )
            },
            onError = {
                Log.e(TAG, "${it.message}")
            }
        )

    }
}


fun NavController.navigateToLogin(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(LOGIN_ROUTE, builder)
}
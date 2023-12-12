package com.folkmanis.laiks.ui.screens.login.login_select

import android.util.Log
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.folkmanis.laiks.R
import com.folkmanis.laiks.utilities.onetap.OneTapSignInWithGoogle
import com.folkmanis.laiks.utilities.onetap.rememberOneTapSignInState

const val LOGIN_SELECT_ROUTE = "LoginSelect"
private const val TAG = "loginSelectScreen"
fun NavGraphBuilder.loginSelectScreen(
    setTitle: (String) -> Unit,
    windowWidth: WindowWidthSizeClass,
    onLoginWithEmail: () -> Unit,
    onRegisterWithEmail: () -> Unit,
    onLogin: () -> Unit,
    onLaiksUserCreated: () -> Unit,
) {

    composable(LOGIN_SELECT_ROUTE) {

        val viewModel: LoginSelectViewModel = hiltViewModel()

        val title = stringResource(R.string.login_title)
        LaunchedEffect(title, setTitle) {
            setTitle(title)
        }

        val googleLoginState = rememberOneTapSignInState()
        val context = LocalContext.current

        val isHorizontal = windowWidth != WindowWidthSizeClass.Compact

        var existingUser by rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(true) {
            viewModel.logout(context)
        }

        OneTapSignInWithGoogle(
            state = googleLoginState,
            clientId = stringResource(R.string.one_tap_web_client_id),
            onTokenIdReceived = { tokenId ->
                Log.d(TAG, "Token: $tokenId")
                if (existingUser)
                    viewModel.loginWithGoogle(tokenId, onLogin)
                else
                    viewModel.registerWithGoogle(tokenId, onLaiksUserCreated)
            },
            onError = {
                Log.e(TAG, "${it.message}")
            }
        )

        LoginSelectScreen(
            onLoginWithEmail = onLoginWithEmail,
            onLoginWithGoogle = {
                existingUser = true
                googleLoginState.open()
            },
            onRegisterWithEmail = onRegisterWithEmail,
            onRegisterWithGoogle = {
                existingUser = false
                googleLoginState.open()
            },
            isHorizontal = isHorizontal
        )

    }
}

package com.folkmanis.laiks.ui.screens.login.login_select

import android.util.Log
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.LaunchedEffect
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

        LaunchedEffect(true) {
            viewModel.logout(context)
        }

        OneTapSignInWithGoogle(
            state = googleLoginState,
            clientId = stringResource(R.string.one_tap_web_client_id),
            onCredential = { credential ->
                credential.googleIdToken?.also { tokenId ->
                    Log.d(TAG, "Token: $tokenId")
                    viewModel.registerWithGoogle(
                        tokenId,
                        credential.displayName,
                        credential.profilePictureUri,
                        onLaiksUserCreated,
                        onLogin,
                    )
                }

            },
            onError = {
                Log.e(TAG, "${it.message}")
            }
        )

        LoginSelectScreen(
            onLoginWithEmail = onLoginWithEmail,
            onLoginWithGoogle = {
                googleLoginState.open()
            },
            onRegisterWithEmail = onRegisterWithEmail,
            isHorizontal = isHorizontal,
        )

    }
}

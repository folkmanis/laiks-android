package com.folkmanis.laiks.ui.screens.user_menu

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.folkmanis.laiks.model.LaiksUser
import com.folkmanis.laiks.utilities.oauth.getSignInIntent


@Composable
fun UserMenu(
    onUsersAdmin: () -> Unit,
    onAppliancesAdmin: () -> Unit,
    onPopToStart: () -> Unit,
    onUserSettings: () -> Unit,
    user: LaiksUser?,
    modifier: Modifier = Modifier,
    viewModel: UserMenuViewModel = hiltViewModel(),
) {

    val uiState = viewModel.uiState
        .collectAsStateWithLifecycle(initialValue = UserMenuUiState.NotLoggedIn)
        .value

    LaunchedEffect(user) {
        viewModel.setUser(user)
    }

    val context = LocalContext.current

    val loginLauncher = rememberLauncherForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.login()
        }
    }

    if (uiState is UserMenuUiState.LoggedIn) {
        LoggedInUserMenu(
            onLogout = {
                viewModel.logout(context)
                onPopToStart()
            },
            isVat = uiState.includeVat,
            onSetVat = {
                viewModel.setVat(
                    userId = uiState.userId,
                    value = !uiState.includeVat,
                )
            },
            onUsersAdmin = onUsersAdmin,
            onAppliancesAdmin = onAppliancesAdmin,
            state = uiState,
            onNpPricesReload = viewModel::updateNpPrices,
            onUserSettings = onUserSettings,
            modifier = modifier,
        )
    } else {
        NotLoggedInUserMenu(
            onLogin = { loginLauncher.launch(getSignInIntent()) },
            modifier = modifier,
        )
    }
}
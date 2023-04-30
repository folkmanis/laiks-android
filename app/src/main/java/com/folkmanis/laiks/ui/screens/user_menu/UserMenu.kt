package com.folkmanis.laiks.ui.screens.user_menu

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.folkmanis.laiks.utilities.oauth.getSignInIntent


@Composable
fun UserMenu(
    onUsersAdmin: () -> Unit,
    onAppliancesAdmin: () -> Unit,
    onPopToStart: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UserMenuViewModel = hiltViewModel(),
) {

    val isVat by viewModel.isVat.collectAsStateWithLifecycle(initialValue = true)

    val uiState = viewModel.uiState
        .collectAsStateWithLifecycle(initialValue = UserMenuUiState.NotLoggedIn)
        .value
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
            isVat = isVat,
            onSetVat = {
                viewModel.setVat(!isVat)
            },
            onUsersAdmin = onUsersAdmin,
            onAppliancesAdmin = onAppliancesAdmin,
            state = uiState,
            onNpPricesReload = viewModel::updateNpPrices,
            modifier = modifier,
        )
    } else {
        NotLoggedInUserMenu(
            onLogin = { loginLauncher.launch(getSignInIntent()) },
            modifier = modifier,
        )
    }
}
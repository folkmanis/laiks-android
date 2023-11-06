package com.folkmanis.laiks.ui.screens.user_menu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.folkmanis.laiks.model.LaiksUser


@Composable
fun UserMenu(
    onPopToStart: () -> Unit,
    onUserSettings: () -> Unit,
    onEditAppliances: () -> Unit,
    onLogin: () -> Unit,
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

    if (uiState is UserMenuUiState.LoggedIn) {
        LoggedInUserMenu(
            onLogout = {
                viewModel.logout(context)
                onPopToStart()
            },
            isVat = uiState.includeVat,
            onSetVat = {
                viewModel.setVat(
                    value = !uiState.includeVat,
                )
            },
            state = uiState,
            onUserSettings = onUserSettings,
            onEditAppliances = onEditAppliances,
            modifier = modifier,
        )
    } else {
        NotLoggedInUserMenu(
            onLogin = onLogin,
            modifier = modifier,
        )
    }
}
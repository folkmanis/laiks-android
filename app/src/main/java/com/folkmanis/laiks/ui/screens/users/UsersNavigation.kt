package com.folkmanis.laiks.ui.screens.users

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.folkmanis.laiks.LaiksAppState

private const val ROUTE = "Users"

fun NavGraphBuilder.usersScreen(
    appState: LaiksAppState,
    onEditUser: (String) -> Unit,
) {
    composable(ROUTE) {
        val viewModel: UsersViewModel = hiltViewModel()
        val state by
        viewModel.uiState.collectAsStateWithLifecycle(initialValue = UsersUiState.Loading)
        UsersScreen(
            onEdit = onEditUser,
            state = state,
            popUp = appState::navigateToDefault,
            actions = { appState.AppUserMenu() },
        )
    }
}

fun NavController.navigateToUsers(navOptions: NavOptions? = null) {
    navigate(ROUTE, navOptions)
}
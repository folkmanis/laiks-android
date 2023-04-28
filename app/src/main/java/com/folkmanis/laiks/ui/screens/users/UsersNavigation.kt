package com.folkmanis.laiks.ui.screens.users

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.folkmanis.laiks.R

private const val ROUTE = "Users"

fun NavGraphBuilder.usersScreen(
    onEditUser: (String) -> Unit,
    setTitle: (String) -> Unit,
) {
    composable(ROUTE) {

        val title = stringResource(id = R.string.users_screen)
        LaunchedEffect(Unit) {
            setTitle(title)
        }

        UsersScreen(onEdit = onEditUser)

    }
}

fun NavController.navigateToUsers(builder: (NavOptionsBuilder.() -> Unit) = {}) {
    navigate(ROUTE, builder)
}
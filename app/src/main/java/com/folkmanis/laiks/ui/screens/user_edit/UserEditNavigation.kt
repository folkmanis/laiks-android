package com.folkmanis.laiks.ui.screens.user_edit

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

private const val ROUTE = "UserEditor"
private const val USER_ID = "userId"

fun NavGraphBuilder.userEditScreen(
    setTitle: (String) -> Unit,
) {

    composable(
        route = "$ROUTE/{$USER_ID}",
        arguments = listOf(
            navArgument(USER_ID) {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->

        val userId = backStackEntry.arguments?.getString(USER_ID)
        val viewModel: UserEditViewModel = hiltViewModel()

        LaunchedEffect(userId) {
            if (userId != null) {
                viewModel.loadUser(userId)
            }
        }

        UserEditScreen(
            viewModel = viewModel,
            setTitle = setTitle,
        )
    }

}

fun NavController.editUser(userId: String, navOptions: NavOptions? = null) {
    navigate("$ROUTE/$userId", navOptions)
}
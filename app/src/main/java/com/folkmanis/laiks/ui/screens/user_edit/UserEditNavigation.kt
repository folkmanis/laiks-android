package com.folkmanis.laiks.ui.screens.user_edit

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.folkmanis.laiks.LaiksAppState

private const val ROUTE = "UserEditor"
private const val USER_ID = "userId"

fun NavGraphBuilder.userEditScreen(
    appState: LaiksAppState
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
            onNavigateBack = appState::popUp,
            viewModel = viewModel,
        )
    }

}

fun NavController.editUser(userId: String, navOptions: NavOptions? = null) {
    navigate("$ROUTE/$userId", navOptions)
}
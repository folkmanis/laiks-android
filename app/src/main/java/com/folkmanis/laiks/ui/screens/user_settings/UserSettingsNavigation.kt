package com.folkmanis.laiks.ui.screens.user_settings

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.folkmanis.laiks.R
import com.folkmanis.laiks.utilities.composables.ErrorScreen
import com.folkmanis.laiks.utilities.composables.LoadingScreen

const val ROUTE = "UserSettings"
const val USER_ID = "id"
const val NAME = "name"

fun NavGraphBuilder.userSettingsScreen(
    setTitle: (String) -> Unit,
    onEditAppliances: () -> Unit,
) {

    composable(
        route = "$ROUTE/{$USER_ID}?$NAME={$NAME}",
        arguments = listOf(
            navArgument(USER_ID) {
                type = NavType.StringType
            },
            navArgument(NAME) {
                type = NavType.StringType
                defaultValue = ""
            }
        )
    ) { backStackEntry ->

        val viewModel: UserSettingsViewModel = hiltViewModel()
        viewModel.initialize(backStackEntry.arguments?.getString(USER_ID))

        val name = backStackEntry.arguments?.getString(NAME)

        val title = name ?: stringResource(R.string.user_editor)
        LaunchedEffect(title) {
            setTitle(title)
        }

        val uiState = viewModel
            .uiState
            .collectAsStateWithLifecycle(initialValue = UserSettingsUiState.Loading)
            .value

        when (uiState) {
            is UserSettingsUiState.Loading -> LoadingScreen()
            is UserSettingsUiState.Error -> ErrorScreen(reason = uiState.reason)
            is UserSettingsUiState.Success -> {
                UserSettingsScreen(
                    uiState = uiState,
                    onIncludeVatChange = viewModel::setIncludeVat,
                    onVatChange = viewModel::setVatAmount,
                    onMarketZoneChange = viewModel::setMarketZoneId,
                    onEditAppliances = onEditAppliances,
                )
            }
        }


    }
}

fun NavController.userSettings(
    userId: String,
    name: String?,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    val route = "$ROUTE/$userId?$NAME=$name"
    navigate(route, builder)
}
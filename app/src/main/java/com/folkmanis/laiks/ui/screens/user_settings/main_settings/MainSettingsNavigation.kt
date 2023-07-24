package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.folkmanis.laiks.utilities.composables.ErrorScreen
import com.folkmanis.laiks.utilities.composables.LoadingScreen

const val ROUTE = "MainSettings"

fun NavGraphBuilder.mainSettingsScreen(
    setTitle: (String) -> Unit,
    onUserAppliances: () -> Unit,
) {

    composable(
        route = ROUTE,
    ) {

        val viewModel: UserSettingsViewModel = hiltViewModel()
        viewModel.initialize()

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
                    onEditAppliances = onUserAppliances,
                )
            }
        }


    }
}

fun NavController.mainSettings(
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(ROUTE, builder)
}

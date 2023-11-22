package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.folkmanis.laiks.R
import com.folkmanis.laiks.utilities.composables.ErrorScreen
import com.folkmanis.laiks.utilities.composables.LoadingScreen

const val ROUTE = "MainSettings"

fun NavGraphBuilder.mainSettingsScreen(
    setTitle: (String) -> Unit,
    onUserAppliances: () -> Unit,
    onUserDeleted: () -> Unit,
) {

    composable(
        route = ROUTE,
    ) {

        val viewModel: UserSettingsViewModel = hiltViewModel()
        viewModel.initialize()

        val uiState = viewModel
            .uiState
            .collectAsStateWithLifecycle()
            .value

        val title = composableTitle(state = uiState)
        LaunchedEffect(title) {
            setTitle(title)
        }

        val loginLauncher = rememberLauncherForActivityResult(
            FirebaseAuthUIActivityResultContract()
        ) { result ->
//            viewModel.onLoginResult(result, onLogin)
        }


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
                    onNameChange = viewModel::setName,
                    onDeleteUser = {
                        viewModel.deleteAccount(onUserDeleted)
                    }
                )
            }
        }


    }
}

@Composable
fun composableTitle(state: UserSettingsUiState): String {
    val defaultTitle = stringResource(id = R.string.user_editor)
    val name by remember(state) {
        derivedStateOf {
            if (state is UserSettingsUiState.Success) {
                state.name
            } else {
                defaultTitle
            }
        }
    }
    return name
}

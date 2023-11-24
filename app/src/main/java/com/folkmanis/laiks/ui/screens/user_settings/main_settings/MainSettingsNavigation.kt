package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.folkmanis.laiks.R
import com.folkmanis.laiks.utilities.composables.LoadingScreen
import com.google.firebase.auth.FirebaseUser

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

        val uiState by viewModel
            .uiState
            .collectAsStateWithLifecycle()

        val title = composableTitle(state = uiState)
        LaunchedEffect(title) {
            setTitle(title)
        }

        if (uiState.loading)
            LoadingScreen()
        else
            UserSettingsScreen(
                uiState = uiState,
                onIncludeVatChange = viewModel::setIncludeVat,
                onVatChange = viewModel::setVatAmount,
                onMarketZoneChange = viewModel::setMarketZoneId,
                onEditAppliances = onUserAppliances,
                onNameChange = viewModel::setName,
                onDeleteUser = {
                    viewModel.deleteAccount(
                        onDeleted = onUserDeleted,
                    )
                }
            )

        uiState.userToReAuthenticateAndDelete.also { user ->
            if (user is FirebaseUser) {
                ReAuthenticate(
                    onReAuthenticated = {
                        viewModel.deleteAccount(
                            onDeleted = onUserDeleted,
                        )
                    },
                    onCancel = viewModel::cancelReLogin,
                    user = user,
                )
            }
        }

    }
}

@Composable
fun composableTitle(state: UserSettingsUiState): String {
    val defaultTitle = stringResource(id = R.string.user_editor)
    return state.name.ifEmpty { defaultTitle }
}

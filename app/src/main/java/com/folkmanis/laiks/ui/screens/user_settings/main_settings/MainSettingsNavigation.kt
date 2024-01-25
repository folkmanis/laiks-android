package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.folkmanis.laiks.R
import com.folkmanis.laiks.utilities.composables.LoadingScreen
import com.google.firebase.auth.FirebaseUser

const val ROUTE = "MainSettings"
const val SHOULD_SET_ZONE = "zone_required"
const val NEXT_ROUTE = "next_route"

fun NavGraphBuilder.mainSettingsScreen(
    setTitle: (String) -> Unit,
    onUserAppliances: () -> Unit,
    onUserDeleted: () -> Unit,
    onMarketZoneSet: (String) -> Unit,
    onMarketZoneNotSet: () -> Unit,
) {

    composable(
        route = ROUTE,
        arguments = listOf(
            navArgument(SHOULD_SET_ZONE) {
                type = NavType.BoolType
                defaultValue = false
            },
            navArgument(NEXT_ROUTE) {
                type = NavType.StringType
                nullable = true
            }
        )
    ) { backStackEntry ->

        val viewModel: UserSettingsViewModel = hiltViewModel()

        LaunchedEffect(Unit) {
            viewModel.initialize(
                shouldSetZone = backStackEntry.arguments?.getBoolean(SHOULD_SET_ZONE) ?: false,
                nextRoute = backStackEntry.arguments?.getString(NEXT_ROUTE),
            )
        }

        val uiState by viewModel
            .uiState
            .collectAsStateWithLifecycle()

        val title = stringResource(id = R.string.user_editor)
        LaunchedEffect(title, setTitle) {
            setTitle(title)
        }

        when (val state = uiState) {
            is UserSettingsUiState.Loading ->
                LoadingScreen()

            is UserSettingsUiState.Success -> {
                UserSettingsScreen(
                    uiState = state,
                    onIncludeVatChange = viewModel::setIncludeVat,
                    onVatChange = viewModel::setVatAmount,
                    onMarketZoneChange = {
                        viewModel.setMarketZoneId(
                            value = it,
                            onMarketZoneSet = onMarketZoneSet,
                            onMarketZoneNotSet = onMarketZoneNotSet
                        )
                    },
                    onEditAppliances = onUserAppliances,
                    onNameChange = viewModel::setName,
                    onSendEmailVerification = viewModel::sendEmailVerification,
                    onDeleteUser = {
                        viewModel.deleteAccount(
                            onDeleted = onUserDeleted,
                        )
                    },
                    onEditMarketZone = { viewModel.setMarketZoneEditState(true) }
                )

                state.userToReAuthenticateAndDelete.also { user ->
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


    }
}

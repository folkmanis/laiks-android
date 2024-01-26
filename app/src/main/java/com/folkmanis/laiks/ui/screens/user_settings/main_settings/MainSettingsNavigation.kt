package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.folkmanis.laiks.utilities.composables.LoadingScreen
import com.google.firebase.auth.FirebaseUser

const val ROUTE = "MainSettings"
const val NEXT_ROUTE_ON_ZONE_SET = "nextRoute"

fun NavGraphBuilder.mainSettingsScreen(
    setTitle: (String) -> Unit,
    onUserAppliances: () -> Unit,
    onUserDeleted: () -> Unit,
    onMarketZoneSet: (String) -> Unit,
    onMarketZoneNotSet: () -> Unit,
) {

    composable(
        route = "${ROUTE}?$NEXT_ROUTE_ON_ZONE_SET={$NEXT_ROUTE_ON_ZONE_SET}",
        arguments = listOf(
            navArgument(NEXT_ROUTE_ON_ZONE_SET) {
                type = NavType.StringType
                nullable = true
//                defaultValue=null
            }
        )
    ) { backStackEntry ->

        val viewModel: UserSettingsViewModel = hiltViewModel()

//        val nextRoute = backStackEntry.arguments?.getString(NEXT_ROUTE_ON_ZONE_SET)
//        Log.d("NavController", "setMarketZone: $nextRoute")

//        LaunchedEffect(nextRoute) {
//            viewModel.setNextRoute(nextRoute)
//        }
//
        LaunchedEffect(Unit) {
            viewModel.initialize()
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

fun NavController.setMarketZone(
    nextRoute: String,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    val route = "${ROUTE}?$NEXT_ROUTE_ON_ZONE_SET=$nextRoute"
    Log.d("UserSettingsViewModel", "setMarketZone: $route")

    navigate(
        route,
        builder
    )
}
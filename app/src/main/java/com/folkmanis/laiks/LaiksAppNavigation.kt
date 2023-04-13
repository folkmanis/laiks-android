package com.folkmanis.laiks

import androidx.annotation.StringRes
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.folkmanis.laiks.ui.screens.appliance_edit.ApplianceEdit
import com.folkmanis.laiks.ui.screens.appliances.AppliancesScreen
import com.folkmanis.laiks.ui.screens.clock.ClockScreen
import com.folkmanis.laiks.ui.screens.prices.PricesScreen
import com.folkmanis.laiks.ui.screens.user_edit.UserEditScreen
import com.folkmanis.laiks.ui.screens.users.UsersScreen

enum class LaiksScreen(@StringRes val title: Int) {
    Clock(title = R.string.app_name),
    Prices(title = R.string.prices_screen),
    Users(title = R.string.users_screen),
    UserEditor(title = R.string.user_editor) {
        override val route: String
            get() = "$name/{$USER_ID}"
    },
    Appliances(title = R.string.appliances_screen),
    ApplianceEditor(title = R.string.appliance_screen) {
        override val route: String
            get() = "$name/{$APPLIANCE_ID}?$COPY={$COPY}"
    };

    open val route: String = name

    open fun withParam(param: String): String = "$name/$param"

    companion object {

        val defaultScreen = Clock
        fun byRoute(path: String?): LaiksScreen {
            return if (path != null)
                values().find { it.route.startsWith(path) } ?: defaultScreen
            else
                defaultScreen
        }
    }
}

fun NavGraphBuilder.laiksGraph(appState: LaiksAppState) {

    composable(LaiksScreen.Clock.route) {
        ClockScreen(
            onShowPrices = {
                appState.navigate(LaiksScreen.Prices.name)
            }
        )
    }

    composable(LaiksScreen.Prices.route) {
        PricesScreen()
    }

    composable(LaiksScreen.Users.route) {
        UsersScreen(
            onEdit = { user ->
                appState.navigate(
                    LaiksScreen.UserEditor.withParam(user.id)
                )
            }
        )
    }

    composable(
        route = LaiksScreen.UserEditor.route,
        arguments = listOf(navArgument(USER_ID) {
            type = NavType.StringType
        })
    ) {
        UserEditScreen()
    }

    composable(route = LaiksScreen.Appliances.route) {
        AppliancesScreen(
            onEdit = { applianceId ->
                appState.navigate(
                    LaiksScreen.ApplianceEditor.withParam(applianceId)
                )
            },
            onAdd = {
                appState.navigate(
                    LaiksScreen.ApplianceEditor.route
                )
            }
        )
    }

    composable(
        route = LaiksScreen.ApplianceEditor.route,
        arguments = listOf(
            navArgument(APPLIANCE_ID) {
                type = NavType.StringType
            },
            navArgument(COPY) {
                type = NavType.BoolType
                defaultValue = false
            }
        ),
    ) { backStackEntry ->
        val applianceId = backStackEntry.arguments?.getString(APPLIANCE_ID)
        val createCopy = backStackEntry.arguments?.getBoolean(COPY) ?: false
        ApplianceEdit(
            id = applianceId,
            onNavigateBack = appState::popUp,
            createCopy = createCopy,
            onCopy = { id ->
                appState.navigateAndPop(
                    "${LaiksScreen.ApplianceEditor.withParam(id)}?$COPY=true",
                    LaiksScreen.Appliances.route
                )
            }
        )
    }


}
@file:OptIn(ExperimentalMaterial3Api::class)

package com.folkmanis.laiks.ui.screens.laiks

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.folkmanis.laiks.APPLIANCE_ID
import com.folkmanis.laiks.COPY
import com.folkmanis.laiks.R
import com.folkmanis.laiks.USER_ID
import com.folkmanis.laiks.ui.screens.appliance_edit.ApplianceEdit
import com.folkmanis.laiks.ui.screens.appliances.AppliancesScreen
import com.folkmanis.laiks.ui.screens.clock.ClockScreen
import com.folkmanis.laiks.ui.screens.prices.PricesScreen
import com.folkmanis.laiks.ui.screens.user_edit.UserEditScreen
import com.folkmanis.laiks.ui.screens.user_menu.UserMenu
import com.folkmanis.laiks.ui.screens.users.UsersScreen
import com.folkmanis.laiks.utilities.oauth.getSignInIntent

enum class LaiksScreen(@StringRes val title: Int) {
    Clock(title = R.string.app_name),
    Prices(title = R.string.prices_screen),
    Users(title = R.string.users_screen),
    UserEditor(title = R.string.user_editor) {
        override val route: String
            get() = "${super.route}/{$USER_ID}"
    },
    Appliances(title = R.string.appliances_screen),
    ApplianceEditor(title = R.string.appliance_screen) {
        override val route: String
            get() = "${super.route}/{$APPLIANCE_ID}?$COPY={$COPY}"
    };

    open fun withParam(param: String): String = "$name/$param"

    open val route: String = name

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaiksAppScreen(
    modifier: Modifier = Modifier,
) {

    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = LaiksScreen.byRoute(backStackEntry?.destination?.route)


    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val canNavigateBack = navController.previousBackStackEntry != null




    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(id = currentScreen.title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    if (canNavigateBack) {
                        IconButton(onClick = navController::navigateUp) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back_button)
                            )
                        }
                    }
                },
                actions = {
                    UserMenu(
                        onUsersAdmin = {
                            navController.navigate(
                                LaiksScreen.Users.route,
                            ) {
                                launchSingleTop = true
                                popUpTo(LaiksScreen.Clock.route)
                            }
                        },
                        onAppliancesAdmin = {
                            navController.navigate(
                                LaiksScreen.Appliances.route,
                            ) {
                                launchSingleTop = true
                                popUpTo(LaiksScreen.Clock.route)
                            }
                        },
                        onLogout = {
                            navController.popBackStack(
                                LaiksScreen.Clock.route,
                                false
                            )
                        }
                    )
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = LaiksScreen.defaultScreen.route,
            modifier = modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
        ) {

            composable(LaiksScreen.Clock.route) {
                ClockScreen(
                    onShowPrices = {
                        navController.navigate(LaiksScreen.Prices.name)
                    }
                )
            }

            composable(LaiksScreen.Prices.route) {
                PricesScreen()
            }

            composable(LaiksScreen.Users.route) {
                UsersScreen(
                    onEdit = { user ->
                        navController.navigate(
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
                        navController.navigate(
                            LaiksScreen.ApplianceEditor.withParam(applianceId)
                        )
                    },
                    onAdd = {
                        navController.navigate(
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
                        type= NavType.BoolType
                        defaultValue = false
                    }
                ),
            ) { backStackEntry ->
                val applianceId = backStackEntry.arguments?.getString(APPLIANCE_ID)
                val createCopy = backStackEntry.arguments?.getBoolean(COPY) ?: false
                ApplianceEdit(
                    id = applianceId,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    createCopy = createCopy,
                    onCopy = { id ->
                        navController.navigate(
                            "${LaiksScreen.ApplianceEditor.withParam(id)}?$COPY=true"
                        ) {
                            popUpTo(LaiksScreen.Appliances.route)
                        }
                    }
                )
            }


        }
    }

}

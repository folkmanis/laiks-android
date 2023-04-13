@file:OptIn(ExperimentalMaterial3Api::class)

package com.folkmanis.laiks

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.folkmanis.laiks.ui.screens.user_menu.UserMenu
import kotlinx.coroutines.CoroutineScope


@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) =
    remember(navController, coroutineScope) {
        LaiksAppState(navController, coroutineScope)
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaiksAppScreen(
    modifier: Modifier = Modifier,
) {

    val appState = rememberAppState()


/*
    val backStackEntry by appState.navController.currentBackStackEntryAsState()
    val currentScreen = LaiksScreen.byRoute(backStackEntry?.destination?.route)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
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
                    if (appState.canNavigateBack) {
                        IconButton(onClick = appState::popUp) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back_button)
                            )
                        }
                    }
                },
                actions = {
                    UserMenu(
                        onUsersAdmin = appState::navigateToUsersAdmin,
                        onAppliancesAdmin = appState::navigateToAppliancesAdmin,
                        onLogout = {
                            appState.clearAndNavigate(
                                LaiksScreen.defaultScreen.route,
                            )
                        }
                    )
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = modifier
    )
                .nestedScroll(scrollBehavior.nestedScrollConnection),
    */
        NavHost(
            navController = appState.navController,
            startDestination = LaiksScreen.defaultScreen.route,
            modifier = modifier
        ) {

            laiksGraph(appState)

        }
    }


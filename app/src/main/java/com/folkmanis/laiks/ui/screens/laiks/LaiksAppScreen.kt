@file:OptIn(ExperimentalMaterial3Api::class)

package com.folkmanis.laiks.ui.screens.laiks

import androidx.annotation.StringRes
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.folkmanis.laiks.R
import com.folkmanis.laiks.ui.screens.clock.ClockScreen
import com.folkmanis.laiks.ui.screens.prices.PricesScreen

enum class LaiksScreen(@StringRes val title: Int) {
    Clock(title = R.string.app_name),
    Prices(title = R.string.prices_screen)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaiksAppScreen(
    modifier: Modifier = Modifier,
    viewModel: LaiksViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    val isVat by viewModel.isVat.collectAsStateWithLifecycle(initialValue = true)

    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = LaiksScreen.valueOf(
        backStackEntry?.destination?.route ?: LaiksScreen.Clock.name
    )
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val canNavigateBack = navController.previousBackStackEntry != null

    val context = LocalContext.current

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
                    if (uiState is LaiksUiState.LoggedIn) {
                        LoggedInUserMenu(
                            photoUrl = uiState.photoUrl,
                            displayName = uiState.displayName,
                            onLogout = {
                                navController.popBackStack(LaiksScreen.Clock.name, false)
                                viewModel.logout(context)
                            },
                            isVat = isVat,
                            onSetVat = {
                                viewModel.setVat(!isVat)
                            }
                        )
                    } else {
                        NotLoggedUserMenu(
                            onLogin = { viewModel.login(context) },
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
//            startDestination = LaiksScreen.Prices.name,
            startDestination = LaiksScreen.Clock.name,
            modifier = modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
        ) {

            composable(LaiksScreen.Clock.name) {
                ClockScreen(
                    pricesAllowed = uiState is LaiksUiState.LoggedIn && uiState.isPricesAllowed,
                    modifier = Modifier.padding(innerPadding),
                    onShowPrices = {
                        navController.navigate(LaiksScreen.Prices.name)
                    }
                )
            }

            composable(LaiksScreen.Prices.name) {
                PricesScreen()
            }
        }
    }

}
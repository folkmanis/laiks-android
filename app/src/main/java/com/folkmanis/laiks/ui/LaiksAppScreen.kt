@file:OptIn(ExperimentalMaterial3Api::class)

package com.folkmanis.laiks.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.folkmanis.laiks.R

enum class LaiksScreen(@StringRes val title: Int) {
    Clock(title = R.string.app_name),
    Prices(title = R.string.prices_screen)
}

@Composable
fun LaiksAppScreen(
    modifier: Modifier = Modifier,
    viewModel: LaiksViewModel = viewModel(
        factory = LaiksViewModel.Factory
    )
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = LaiksScreen.valueOf(
        backStackEntry?.destination?.route ?: LaiksScreen.Clock.name
    )

    Scaffold(
        topBar = {
            LaiksTopBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                user = uiState.value.user,
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = LaiksScreen.Clock.name,
            modifier = modifier.padding(innerPadding),
        ) {

            composable(LaiksScreen.Clock.name) {
                ClockScreen(
                    pricesAllowed = true,
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

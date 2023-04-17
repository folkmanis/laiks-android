package com.folkmanis.laiks.ui.screens.np_data

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable

private const val ROUTE = "NpPrices"

fun NavGraphBuilder.npDataScreen() {

    composable(ROUTE) {
        NpDataScreen()
    }
}

fun NavController.navigateToNpPrices(builder: NavOptionsBuilder.()->Unit = {}) {
    navigate(ROUTE, builder)
}
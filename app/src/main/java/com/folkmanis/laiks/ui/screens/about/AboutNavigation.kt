package com.folkmanis.laiks.ui.screens.about

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.folkmanis.laiks.R

private const val ROUTE = "About"

fun NavGraphBuilder.aboutScreen(
    setTitle: (String)-> Unit
) {

    composable(ROUTE) {

        val title = stringResource(R.string.about_title)
        LaunchedEffect(title, setTitle) {
            setTitle(title)
        }

        AboutScreen(
            modifier = Modifier.fillMaxSize()
        )

    }
}

fun NavController.navigateToAbout(builder: NavOptionsBuilder.()-> Unit = {}) {
    navigate(ROUTE, builder)
}
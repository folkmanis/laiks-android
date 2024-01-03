package com.folkmanis.laiks.ui.screens.about

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
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
    setTitle: (String)-> Unit,
    windowSize: WindowSizeClass,
) {

    composable(ROUTE) {

        val title = stringResource(R.string.about_title)
        LaunchedEffect(title, setTitle) {
            setTitle(title)
        }

        AboutScreen(
            modifier = Modifier.fillMaxSize(),
            isHorizontal = windowSize.heightSizeClass < WindowHeightSizeClass.Medium
        )

    }
}

fun NavController.navigateToAbout(builder: NavOptionsBuilder.()-> Unit = {}) {
    navigate(ROUTE, builder)
}
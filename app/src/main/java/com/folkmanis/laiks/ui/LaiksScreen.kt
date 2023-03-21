@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.folkmanis.laiks.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.folkmanis.laiks.R
import com.folkmanis.laiks.ui.theme.LaiksTheme

@Composable
fun LaiksScreen(
    signIn: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Scaffold(
        topBar = {
            LaiksTopBar(
                currentScreen = "Laiks",
                canNavigateBack = false,
                navigateUp = {},
                signIn = signIn,
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        ClockScreen(
            modifier = Modifier.padding(innerPadding),
        )
    }

}

@Composable
fun LaiksTopBar(
    currentScreen: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    signIn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text(currentScreen) },
        actions = {
            IconButton(onClick = signIn) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = stringResource(id = R.string.login_button)
                )
            }
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
fun LaiksTopBarPreview() {
    LaiksTheme {
        LaiksTopBar(
            currentScreen = "Laiks",
            canNavigateBack = true,
            navigateUp = { },
            signIn = {}
        )
    }
}
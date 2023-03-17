@file:OptIn(ExperimentalMaterial3Api::class)

package com.folkmanis.laiks

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.folkmanis.laiks.ui.ClockScreen
import java.time.LocalDateTime

@Composable
fun LaiksApp(
    modifier: Modifier = Modifier
) {

    Scaffold(
        topBar = {
            LaiksTopBar(
                currentScreen = "Laiks",
                canNavigateBack = false,
                navigateUp = {},
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        ClockScreen(
            offset = 3,
            onOffsetChange = {},
            modifier = Modifier.padding(innerPadding),
            time = LocalDateTime.now().toLocalTime(),
        )
    }

}

@Composable
fun LaiksTopBar(
    currentScreen: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
) {
    TopAppBar(
        title = { Text(currentScreen) },
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


@file:OptIn(ExperimentalMaterial3Api::class)

package com.folkmanis.laiks

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.folkmanis.laiks.data.FakeUserPreferencesRepository
import com.folkmanis.laiks.ui.ClockScreen
import com.folkmanis.laiks.ui.ClockViewModel
import com.folkmanis.laiks.ui.theme.LaiksTheme

@Composable
fun LaiksScreen(
    modifier: Modifier = Modifier,
    viewModel: ClockViewModel = viewModel(
        factory = ClockViewModel.Factory
    ),
) {

    val uiState by viewModel
        .uiState
        .collectAsStateWithLifecycle()

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
            offset = uiState.offset,
            time = uiState.time,
            onOffsetChange = {
                viewModel.updateOffset(it)
            },
            modifier = Modifier.padding(innerPadding),
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


@Preview
@Composable
fun LaiksScreenPreview() {
    val viewModel = ClockViewModel(
        FakeUserPreferencesRepository
    )
    LaiksTheme() {
        LaiksScreen(viewModel=viewModel)
    }
}

@Preview
@Composable
fun LaiksScreenPreviewDark() {
    val viewModel = ClockViewModel(
        FakeUserPreferencesRepository
    )
    LaiksTheme(
        darkTheme = true,
    ) {
        LaiksScreen(viewModel=viewModel)
    }
}

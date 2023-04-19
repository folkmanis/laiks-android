package com.folkmanis.laiks.ui.screens.np_data

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.folkmanis.laiks.utilities.composables.ErrorScreen
import com.folkmanis.laiks.utilities.composables.LoadingScreen

@Composable
fun NpDataScreen(
    viewModel: NpDataViewModel = hiltViewModel()
) {

    when (val data = viewModel.npData.collectAsStateWithLifecycle().value) {
        is NpDataUiState.Error -> ErrorScreen(reason = data.reason)
        is NpDataUiState.Loading -> LoadingScreen()
        is NpDataUiState.Success -> {
            LazyColumn {
                items(items = data.data) { price ->
                    Row {
                        Text(text = price.startTime.toString())
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = price.value.toString())
                    }
                }
            }

        }

    }
}
package com.folkmanis.laiks.ui.screens.np_data

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.folkmanis.laiks.utilities.ext.toLocalDateTime

@Composable
fun NpDataScreen(
    viewModel: NpDataViewModel = hiltViewModel()
) {

    val data = viewModel.npData.collectAsStateWithLifecycle().value

    Column {
        Button(onClick = {
            viewModel.loadData()
        }) {
            Text(text = "Download")
        }

        if (data is NpDataUiState.Success) {
            data.data.forEach { price ->
                Row {
                    Text(text = price.startTime.toLocalDateTime().toString())
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = price.value.toString())
                }
            }
        }
    }

}

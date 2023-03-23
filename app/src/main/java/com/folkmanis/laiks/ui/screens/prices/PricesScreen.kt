package com.folkmanis.laiks.ui.screens.prices

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.folkmanis.laiks.R
import com.folkmanis.laiks.utilities.composables.ErrorScreen
import com.folkmanis.laiks.utilities.composables.LoadingScreen

@Composable
fun PricesScreen(
    viewModel: PricesViewModel = viewModel(
        factory = PricesViewModel.Factory
    ),
    modifier: Modifier = Modifier,
) {

    val uiState = viewModel.uiState
        .collectAsStateWithLifecycle(PricesUiState.Loading)
        .value

    when (uiState) {
        is PricesUiState.Success -> {
            val prices = uiState.npPrices
            val timeNow = uiState.instantTime
            LazyColumn(
                modifier = modifier
            ) {
                items(
                    prices,
                    key = { it.id }
                ) { price ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        PriceRow(
                            dateNow = timeNow,
                            price = price,
                        )
                    }
                    Divider(thickness = 2.dp)
                }
            }
        }
        is PricesUiState.Loading -> LoadingScreen(modifier = modifier)
        is PricesUiState.Error -> ErrorScreen(reason = uiState.reason, modifier = modifier)
    }

}


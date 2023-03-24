package com.folkmanis.laiks.ui.screens.prices

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
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
            val timeNow = uiState.instantHour
            LazyColumn(
                modifier = modifier
            ) {
                items(
                    prices,
                    key = { it.id }
                ) { price ->
                    PriceRow(
                        dateNow = timeNow,
                        price = price,
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
                    Divider(
                        thickness = 2.dp,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                    )
                }
            }
        }
        is PricesUiState.Loading -> LoadingScreen(modifier = modifier)
        is PricesUiState.Error -> ErrorScreen(reason = uiState.reason, modifier = modifier)
    }

}


package com.folkmanis.laiks.ui.screens.prices

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.folkmanis.laiks.utilities.composables.ErrorScreen
import com.folkmanis.laiks.utilities.composables.LoadingScreen

@Composable
fun PricesScreen(
    modifier: Modifier = Modifier,
    viewModel: PricesViewModel = hiltViewModel(),
) {

    val uiState = viewModel.uiState
        .collectAsStateWithLifecycle(PricesUiState.Loading)
        .value

    when (uiState) {
        is PricesUiState.Success -> {
            LazyColumn(
                modifier = modifier
            ) {
                uiState.groupedCosts.forEach { (date, powerHour) ->
                    item {
                        DateHeader(date = date)
                        Divider(
                            thickness = 2.dp,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                        )
                    }
                    items(powerHour, key = { it.id }) {
                        PriceRow(
                            powerHour = it,
                            average = uiState.average,
                            stDev = uiState.stDev,
                            modifier = Modifier
                                .fillMaxWidth(),
                        )
                        Divider(
                            thickness = 2.dp,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
        is PricesUiState.Loading -> LoadingScreen(modifier = modifier)
        is PricesUiState.Error -> ErrorScreen(reason = uiState.reason, modifier = modifier)
    }

}


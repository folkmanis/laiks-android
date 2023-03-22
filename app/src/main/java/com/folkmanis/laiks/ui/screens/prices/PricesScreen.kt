package com.folkmanis.laiks.ui.screens.prices

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PricesScreen(
    viewModel: PricesViewModel = viewModel(
        factory = PricesViewModel.Factory
    )
) {

    val npPrices by viewModel.npPrices
        .collectAsStateWithLifecycle(emptyList())

    LazyColumn {
        items(
            npPrices,
            key = {it.id}
        ) {
            Text(text = it.toString())
        }
    }

}
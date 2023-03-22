package com.folkmanis.laiks.ui.screens.prices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.folkmanis.laiks.LaiksApplication
import com.folkmanis.laiks.data.PricesService

class PricesViewModel(
    private val pricesService: PricesService
) : ViewModel() {

val npPrices = pricesService.allNpPrices

    companion object {
        private const val TAG = "Prices View Model"
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LaiksApplication)
                PricesViewModel(
                    application.pricesService
                )
            }
        }
    }
}
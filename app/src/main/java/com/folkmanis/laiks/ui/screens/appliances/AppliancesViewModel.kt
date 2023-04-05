package com.folkmanis.laiks.ui.screens.appliances

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.model.PowerAppliance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppliancesViewModel @Inject constructor(
    private val pricesService: PricesService
) : ViewModel() {

    val appliances = pricesService.allAppliancesFlow

    fun delete(id: String) {
        viewModelScope.launch {
            pricesService.deleteAppliance(id)
        }
    }

}
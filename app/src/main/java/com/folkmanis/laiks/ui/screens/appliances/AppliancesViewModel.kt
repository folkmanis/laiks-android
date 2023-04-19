package com.folkmanis.laiks.ui.screens.appliances

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.AppliancesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppliancesViewModel @Inject constructor(
    private val appliancesService: AppliancesService
) : ViewModel() {

    val appliances = appliancesService.allAppliancesFlow

    fun delete(id: String) {
        viewModelScope.launch {
            appliancesService.deleteAppliance(id)
        }
    }

}
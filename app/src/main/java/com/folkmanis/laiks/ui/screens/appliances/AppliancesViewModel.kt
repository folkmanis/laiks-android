package com.folkmanis.laiks.ui.screens.appliances

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.AppliancesService
import com.folkmanis.laiks.data.domain.IsPermissionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppliancesViewModel @Inject constructor(
    private val appliancesService: AppliancesService,
    isPermission: IsPermissionUseCase,
) : ViewModel() {

    val appliances = appliancesService.allAppliancesFlow

    val isAdmin = isPermission("admin")

    fun delete(id: String) {
        viewModelScope.launch {
            appliancesService.deleteAppliance(id)
        }
    }

}
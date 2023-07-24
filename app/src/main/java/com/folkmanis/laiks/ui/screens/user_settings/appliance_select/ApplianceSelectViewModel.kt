package com.folkmanis.laiks.ui.screens.user_settings.appliance_select

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.AppliancesService
import com.folkmanis.laiks.data.LaiksUserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ApplianceSelectViewModel @Inject constructor(
    appliancesService: AppliancesService,
    laiksUserService: LaiksUserService,
) : ViewModel() {

    val userAppliances = laiksUserService.laiksUserFlow()
        .filterNotNull()
        .map { it.appliances }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

    val systemAppliances = appliancesService.allAppliancesFlow
        .map { appliances -> appliances.filter { it.enabled } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

}

package com.folkmanis.laiks.ui.screens.user_settings.appliance_select

import androidx.lifecycle.ViewModel
import com.folkmanis.laiks.data.AppliancesService
import com.folkmanis.laiks.data.LaiksUserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ApplianceSelectViewModel @Inject constructor(
    appliancesService: AppliancesService,
    laiksUserService: LaiksUserService,
) : ViewModel() {

    val userAppliances = laiksUserService.laiksUserFlow()
        .filterNotNull()
        .map { it.appliances }

    val systemAAppliances = appliancesService.allAppliancesFlow
        .map { appliances -> appliances.filter { it.enabled } }

}

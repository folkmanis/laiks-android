package com.folkmanis.laiks.ui.screens.appliances

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.AppliancesService
import com.folkmanis.laiks.data.domain.IsPermissionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.folkmanis.laiks.data.domain.ActiveAppliancesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class AppliancesViewModel @Inject constructor(
    private val appliancesService: AppliancesService,
    private val activeAppliances: ActiveAppliancesUseCase,
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<AppliancesUiState>(AppliancesUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadAppliances()
    }

    private fun loadAppliances() {
        viewModelScope.launch {
            val appliances = activeAppliances()
            _uiState.value = AppliancesUiState.Success(appliances)
        }
    }

    fun delete(idx:Int) {
        viewModelScope.launch {
//            appliancesService.deleteAppliance(id)
        }
    }

}
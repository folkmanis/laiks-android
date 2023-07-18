package com.folkmanis.laiks.ui.screens.appliances

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.AppliancesService
import com.folkmanis.laiks.data.LaiksUserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.folkmanis.laiks.data.domain.ActiveAppliancesUseCase
import com.folkmanis.laiks.model.ApplianceRecord
import com.folkmanis.laiks.model.PowerApplianceRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

fun PowerApplianceRecord.toApplianceRecord() =
    ApplianceRecord(type = type, applianceId = id)

@HiltViewModel
class AppliancesViewModel @Inject constructor(
    private val appliancesService: AppliancesService,
    private val activeAppliances: ActiveAppliancesUseCase,
    private val laiksUserService: LaiksUserService,
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

    fun delete(idx: Int) {
        _uiState.update { state ->
            if (state is AppliancesUiState.Success) {
                state.removeRecordAt(idx)
            } else state
        }
        viewModelScope.launch {
            saveSettings()
        }
    }

    fun addAppliance(record: PowerApplianceRecord) {
        _uiState.update { state ->
            if (state is AppliancesUiState.Success) {
                state.addRecord(record)
            } else
                state
        }
        viewModelScope.launch {
            saveSettings()
        }
    }

    private suspend fun saveSettings() {
        val state = _uiState.value
        if (state is AppliancesUiState.Success) {
            state.records.map {
                it.toApplianceRecord()
            }.apply {
                laiksUserService.updateLaiksUser("appliances", this)
            }
        }
    }

}
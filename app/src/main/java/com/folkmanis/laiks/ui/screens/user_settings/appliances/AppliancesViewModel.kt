package com.folkmanis.laiks.ui.screens.user_settings.appliances

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.LaiksUserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppliancesViewModel @Inject constructor(
    private val laiksUserService: LaiksUserService,
) : ViewModel() {

    var uiState = MutableStateFlow(AppliancesUiState())
        private set

    init {
        viewModelScope.launch {
            laiksUserService.laiksUserFlow()
                .filterNotNull()
                .map { it.appliances }
                .collect { appliances ->
                    uiState.update { state ->
                        state.copy(
                            appliances = appliances,
                            loading = false,
                        )
                    }
                }

        }
    }

    fun delete(idx: Int) {
        uiState.update {
            it.copy(saving = true)
        }
        uiState.update { it.removeRecordAt(idx) }

        viewModelScope.launch {
            laiksUserService.updateLaiksUser("appliances", uiState.value.appliances)
            uiState.update { it.copy(saving = false) }
        }

    }


}
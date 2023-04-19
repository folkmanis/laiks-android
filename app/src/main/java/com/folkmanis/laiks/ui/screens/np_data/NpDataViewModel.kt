package com.folkmanis.laiks.ui.screens.np_data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.NpRepository
import com.folkmanis.laiks.model.np_data.ZonedNpPrice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

sealed interface NpDataUiState {
    data class Error(val reason: String) : NpDataUiState
    object Loading : NpDataUiState
    data class Success(
        val data: List<ZonedNpPrice>
    ) : NpDataUiState
}

@HiltViewModel
class NpDataViewModel @Inject constructor(
    private val npRepository: NpRepository
) : ViewModel() {

    private val _npData = MutableStateFlow<NpDataUiState>(NpDataUiState.Loading)
    val npData = _npData.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                val data = npRepository.getNpData().data.toZonedNpPrices()
                _npData.update { NpDataUiState.Success(data) }
            } catch (err: IOException) {
                _npData.update { NpDataUiState.Error(err.toString()) }
            }
        }
    }
}
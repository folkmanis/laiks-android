package com.folkmanis.laiks.ui.screens.np_data

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.NpRepository
import com.folkmanis.laiks.model.NpPrice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NpDataViewModel @Inject constructor(
    private val npRepository: NpRepository
) : ViewModel() {

    private val _npData = MutableStateFlow<List<NpPrice>>(emptyList())
    val npData = _npData.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            val data = npRepository.getNpData()
            _npData.update { data }
        }
    }
}
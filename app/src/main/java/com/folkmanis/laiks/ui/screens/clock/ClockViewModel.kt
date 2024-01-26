package com.folkmanis.laiks.ui.screens.clock

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.MAX_APPLIANCES_ON_CLOCK_SCREEN
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.UserPreferencesRepository
import com.folkmanis.laiks.utilities.minuteTicks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClockViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val laiksUserService: LaiksUserService,
) : ViewModel() {

    var uiState by mutableStateOf(ClockUiState())
        private set

    fun initialize() {
        viewModelScope.launch {
            laiksUserService.laiksUserFlow().collect { laiksUser ->
                uiState = uiState.copy(
                    appliances = laiksUser?.run {
                        appliances
                            .take(MAX_APPLIANCES_ON_CLOCK_SCREEN)
                    } ?: emptyList()
                )
            }
        }

        viewModelScope.launch {
            laiksUserService.npAllowedFlow.collect {
                uiState = uiState.copy(isPricesAllowed = it)
            }
        }

        viewModelScope.launch {
            userPreferencesRepository.savedTimeOffset.collect {
                uiState = uiState.copy(offset = it)
            }
        }

        viewModelScope.launch {
            minuteTicks().collect { localDateTime ->
                uiState = uiState.copy(
                    currentTime = localDateTime.toLocalTime(),
                )
            }
        }
    }

    fun updateOffset(value: Int) {
        viewModelScope.launch {
            userPreferencesRepository
                .saveTimeOffset(uiState.offset + value)
        }
    }

    companion object {
        @Suppress("unused")
        private const val TAG = "ClockViewModel"
    }

}
package com.folkmanis.laiks.ui.screens.clock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.UserPreferencesRepository
import com.folkmanis.laiks.utilities.minuteTicks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClockViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    laiksUserService: LaiksUserService,
) : ViewModel() {

    val isPricesAllowed = laiksUserService.npAllowedFlow

    val appliances = laiksUserService.laiksUserFlow()
        .map { user-> user?.appliances?.take(2) ?: emptyList() }


    val offsetState: StateFlow<Int> =
        userPreferencesRepository.savedTimeOffset
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000L),
                0
            )

    val timeState = combine(offsetState, minuteTicks()) { offset, minute ->
        minute.toLocalTime().plusHours(offset.toLong())
    }

    fun updateOffset(value: Int) {
        viewModelScope.launch {
            userPreferencesRepository
                .saveTimeOffset(offsetState.value + value)
        }
    }

    companion object {
        @Suppress("unused")
        private const val TAG = "ClockViewModel"
    }

}
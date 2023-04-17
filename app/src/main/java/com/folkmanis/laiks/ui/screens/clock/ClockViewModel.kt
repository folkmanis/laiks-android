package com.folkmanis.laiks.ui.screens.clock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.UserPreferencesRepository
import com.folkmanis.laiks.utilities.minuteTicks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClockViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    accountService: AccountService,
    ) : ViewModel() {

    val isPricesAllowed = accountService.laiksUserFlow
        .map { laiksUser -> laiksUser?.npAllowed ?: false }

    val uiState: StateFlow<ClockUiState> = combine(
        minuteTicks(),
        userPreferencesRepository.savedTimeOffset
    ) { time, offset ->
        ClockUiState(
            time.toLocalTime().plusHours(offset.toLong()),
            offset
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ClockUiState(),
        )

    fun updateOffset(value: Int) {
        val oldOffset = uiState.value.offset
        viewModelScope.launch {
            userPreferencesRepository
                .saveTimeOffset(oldOffset + value)
        }
    }


}
package com.folkmanis.laiks.ui.screens.clock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.AppliancesService
import com.folkmanis.laiks.data.UserPreferencesRepository
import com.folkmanis.laiks.utilities.minuteTicks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClockViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    accountService: AccountService,
    appliancesService: AppliancesService,
) : ViewModel() {

    val isPricesAllowed = accountService.laiksUserFlow
        .map { laiksUser -> laiksUser?.npAllowed ?: false }

    val appliances = accountService.laiksUserFlow
        .filter { it?.npAllowed ?: false }
        .map { appliancesService.activeAppliances() }

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
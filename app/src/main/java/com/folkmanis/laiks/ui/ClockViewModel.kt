package com.folkmanis.laiks.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.folkmanis.laiks.LaiksApplication
import com.folkmanis.laiks.data.UserPreferencesRepository
import com.folkmanis.laiks.utilities.delayToNextMinute
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime

data class ClockUiState(
    val time: LocalTime = LocalTime.now(),
    val offset: Int = 0,
)

class ClockViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    private val clockTicksFlow = flow<LocalDateTime> {
        while (true) {
            val now = LocalDateTime.now()
            emit(now)
            val delayToNext = delayToNextMinute(now)
            delay(delayToNext)
        }
    }

    val uiState: StateFlow<ClockUiState> = combine(
        clockTicksFlow,
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

    companion object {
        val Factory: ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val application = (this[APPLICATION_KEY] as LaiksApplication)
                    ClockViewModel(
                        application.userPreferencesRepository,
                    )
                }
            }
    }

}
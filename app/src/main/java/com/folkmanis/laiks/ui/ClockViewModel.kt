package com.folkmanis.laiks.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.folkmanis.laiks.LaiksApplication
import com.folkmanis.laiks.data.ClockTicksRepository
import com.folkmanis.laiks.data.UserPreferencesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalTime

data class ClockUiState(
    val time: LocalTime = LocalTime.now(),
    val offset: Int = 0,
)

class ClockViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    clockTicksRepository: ClockTicksRepository,
) : ViewModel() {

    val uiState: StateFlow<ClockUiState> = combine(
        clockTicksRepository.clockTicksFlow(),
        userPreferencesRepository.savedTimeOffset
    ) { time, offset ->
        ClockUiState(
            time.toLocalTime().plusHours(offset.toLong()),
            offset
        )
    }
        .map {
            Log.d(TAG, "Next tick ${it.time}")
            it
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
        val TAG = "Clock View Model"
        val Factory: ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val application = (this[APPLICATION_KEY] as LaiksApplication)
                    ClockViewModel(
                        application.userPreferencesRepository,
                        application.clocksTicksRepository,
                    )
                }
            }
    }

}
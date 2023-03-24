package com.folkmanis.laiks.ui.screens.prices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.folkmanis.laiks.LaiksApplication
import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.utilities.SHOW_HOURS_BEFORE
import com.folkmanis.laiks.utilities.hourTicks
import com.folkmanis.laiks.utilities.minuteTicks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.*
import java.io.IOException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class PricesViewModel(
    private val pricesService: PricesService
) : ViewModel() {

    val uiState = hourTicks()
        .flatMapLatest { localDateTime ->
            pricesService.allNpPrices(startTime(localDateTime))
                .catch {
                    if (it is FirebaseFirestoreException)
                        PricesUiState.Error(it.message ?: "Firebase error", it)
                    else
                        throw it
                }
                .flatMapLatest { npPrices ->
                    minuteTicks()
                        .flatMapLatest { minute ->
                            pricesService.activeAppliances
                                .map { appliances ->
                                    PricesUiState.Success(
                                        hour = localDateTime,
                                        npPrices = npPrices,
                                        minute = minute,
                                    )
                                }
                        }
                }
        }

    private fun startTime(localDateTime: LocalDateTime): Timestamp {
        val instant: Instant = localDateTime
            .minusHours(SHOW_HOURS_BEFORE)
            .truncatedTo(ChronoUnit.HOURS)
            .atZone(ZoneId.systemDefault())
            .toInstant()
        return Timestamp(instant.epochSecond, 0)
    }

    companion object {
        private const val TAG = "Prices View Model"
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LaiksApplication)
                PricesViewModel(
                    application.pricesService
                )
            }
        }
    }
}
package com.folkmanis.laiks.ui.screens.prices

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.INCLUDE_AVERAGE_DAYS
import com.folkmanis.laiks.R
import com.folkmanis.laiks.REFRESH_AT_HOURS
import com.folkmanis.laiks.REFRESH_AT_MINUTES
import com.folkmanis.laiks.REFRESH_AT_TZ
import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.data.domain.AppliancesCostsUseCase
import com.folkmanis.laiks.data.domain.LastDaysPricesUseCase
import com.folkmanis.laiks.data.domain.NpUpdateUseCase
import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.PowerApplianceHour
import com.folkmanis.laiks.utilities.ext.*
import com.folkmanis.laiks.utilities.hourTicks
import com.folkmanis.laiks.utilities.millisecondsToNextUpdate
import com.folkmanis.laiks.utilities.minuteTicks
import com.folkmanis.laiks.utilities.snackbar.SnackbarManager
import com.folkmanis.laiks.utilities.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class PricesViewModel @Inject constructor(
    lastDaysPrices: LastDaysPricesUseCase,
    appliancesCosts: AppliancesCostsUseCase,
    private val pricesRepository: PricesService,
    private val npUpdate: NpUpdateUseCase,
) : ViewModel(), DefaultLifecycleObserver {

    private val _checkForUpdatesNow = MutableStateFlow(false)
    val checkForUpdatesNow = _checkForUpdatesNow.asStateFlow()

    private var updateCheckJob: Job? = null
    override fun onResume(owner: LifecycleOwner) {
        updateCheckJob = createUpdateCheckJob()
        Log.d(TAG, "onResume")
    }

    override fun onPause(owner: LifecycleOwner) {
        Log.d(TAG, "onPause")
        updateCheckJob?.cancel()
    }

    private fun createUpdateCheckJob(): Job = viewModelScope.launch {
        val lastUpdateTime = pricesRepository.lastUpdate()
        val toNext = millisecondsToNextUpdate(
            Instant.now(),
            lastUpdateTime,
            REFRESH_AT_HOURS, REFRESH_AT_MINUTES, REFRESH_AT_TZ
        )
        Log.d(TAG, "Delayed for $toNext ms")
        delay(toNext)
        Log.d(TAG, "_checkForUpdatesNow = true")
        _checkForUpdatesNow.value = true
    }

    fun checkForUpdate() {
        _checkForUpdatesNow.value = false
        Log.d(TAG, "Checking for update")
        SnackbarManager.showMessage(R.string.retrieving_prices)
        viewModelScope.launch {
            try {
                val newRecords = npUpdate()
                Log.d(TAG, "$newRecords retrieved")
                SnackbarManager.showMessage(
                    R.plurals.prices_retrieved,
                    newRecords,
                    newRecords
                )
            } catch (err: Throwable) {
                SnackbarManager.showMessage(
                    err.toSnackbarMessage()
                )
                Log.e(TAG, "Error: $err")
            }
            updateCheckJob = createUpdateCheckJob()
        }
    }

    private val lastPricesFlow: Flow<List<NpPrice>> =
        lastDaysPrices(INCLUDE_AVERAGE_DAYS)
            .shareIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000L)
            )

    val pricesStatistics: Flow<PricesStatistics> =
        lastPricesFlow.statisticsFlow

    val uiState: Flow<PricesUiState> = combine(hourTicks(), lastPricesFlow) { hour, prices ->
        val futurePrices = prices
            .filter { it.startTime.toLocalDateTime() >= hour }
        if (futurePrices.isEmpty())
            PricesUiState.Loading
        else
            PricesUiState.Success(
                futurePrices.groupBy { it.startTime.toLocalDateTime().toLocalDate() },
                hour
            )
    }

    val appliancesState: Flow<Map<Int, List<PowerApplianceHour>>> =
        combine(minuteTicks(), lastPricesFlow) { minute, prices ->
            appliancesCosts(prices, minute)
        }

    companion object {
        @Suppress("unused")
        private const val TAG = "PricesViewModel"
    }
}
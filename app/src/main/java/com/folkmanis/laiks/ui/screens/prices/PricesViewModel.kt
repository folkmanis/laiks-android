package com.folkmanis.laiks.ui.screens.prices

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.domain.AppliancesCostsUseCase
import com.folkmanis.laiks.data.domain.DelayToNextNpUpdateUseCase
import com.folkmanis.laiks.data.domain.HourlyPricesUseCase
import com.folkmanis.laiks.data.domain.NpUpdateUseCase
import com.folkmanis.laiks.data.domain.StatisticsUseCase
import com.folkmanis.laiks.model.PowerApplianceHour
import com.folkmanis.laiks.utilities.ext.*
import com.folkmanis.laiks.utilities.hourTicks
import com.folkmanis.laiks.utilities.minuteTicks
import com.folkmanis.laiks.utilities.snackbar.SnackbarManager
import com.folkmanis.laiks.utilities.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PricesViewModel @Inject constructor(
    appliancesCosts: AppliancesCostsUseCase,
    private val npUpdate: NpUpdateUseCase,
    private val delayToNextNpUpdate: DelayToNextNpUpdateUseCase,
    statistics: StatisticsUseCase,
    hourlyPrices: HourlyPricesUseCase,
) : ViewModel(), DefaultLifecycleObserver {

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

        val toNext = delayToNextNpUpdate() + Random.nextLong(10000)

        Log.d(TAG, "Delayed for $toNext ms")

        delay(toNext)

        checkForUpdate()
    }

    private suspend fun checkForUpdate() {

        Log.d(TAG, "Checking for update")

        try {
            if (delayToNextNpUpdate() == 0L) {
                SnackbarManager.showMessage(R.string.retrieving_prices)
                val newRecords = npUpdate()
                Log.d(TAG, "$newRecords retrieved")
                SnackbarManager.showMessage(
                    R.plurals.prices_retrieved,
                    newRecords,
                    newRecords
                )
            }
        } catch (err: Throwable) {
            SnackbarManager.showMessage(
                err.toSnackbarMessage()
            )
            Log.e(TAG, "Error: $err")
        }
        updateCheckJob = createUpdateCheckJob()
    }

    val pricesStatistics: Flow<PricesStatistics> = statistics()

    val uiState: Flow<PricesUiState> = hourTicks()
        .flatMapLatest { hour ->
            hourlyPrices(hour)
                .map { prices ->
                    if (prices.isEmpty())
                        PricesUiState.Loading
                    else
                        PricesUiState.Success(
                            prices.groupBy { it.startTime.toLocalDateTime().toLocalDate() },
                            hour
                        )

                }
        }

    val appliancesState: Flow<Map<Int, List<PowerApplianceHour>>> = minuteTicks()
        .flatMapLatest { minute ->
            hourlyPrices(minute)
                .map { prices ->
                    appliancesCosts(prices, minute)
                }
        }

    companion object {
        @Suppress("unused")
        private const val TAG = "PricesViewModel"
    }
}
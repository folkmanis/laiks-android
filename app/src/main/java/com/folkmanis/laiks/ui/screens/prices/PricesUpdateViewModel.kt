package com.folkmanis.laiks.ui.screens.prices

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.domain.DelayToNextNpUpdateUseCase
import com.folkmanis.laiks.data.domain.NpUpdateUseCase
import com.folkmanis.laiks.utilities.snackbar.SnackbarManager
import com.folkmanis.laiks.utilities.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

open class PricesUpdateViewModel(
    private val npUpdate: NpUpdateUseCase,
    private val delayToNextNpUpdate: DelayToNextNpUpdateUseCase,
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

    companion object {
        const val TAG = "PricesUpdateViewModel"
    }
}
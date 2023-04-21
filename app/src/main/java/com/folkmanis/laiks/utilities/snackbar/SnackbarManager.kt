package com.folkmanis.laiks.utilities.snackbar

import android.util.Log
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SnackbarManager {

    private val messages: MutableStateFlow<SnackbarMessage?> = MutableStateFlow(null)
    val snackbarMessages: StateFlow<SnackbarMessage?>
        get() = messages.asStateFlow()

    fun showMessage(@StringRes message: Int) {
        messages.value = SnackbarMessage.ResourceSnackbar(message)
    }

    fun showMessage(@PluralsRes message: Int, count: Int, vararg formatArgs: Any) {
        Log.d(TAG, "count: $count; $*formatArgs")
        messages.value =
            SnackbarMessage.PluralsSnackbar(message, count, *formatArgs)
    }

    fun showMessage(message: String) {
        messages.value = SnackbarMessage.StringSnackbar(message)
    }

    fun showMessage(message: SnackbarMessage) {
        messages.value = message
    }

    const val TAG = "SnackbarManager"
}
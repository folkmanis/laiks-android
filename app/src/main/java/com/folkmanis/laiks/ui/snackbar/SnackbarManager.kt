package com.folkmanis.laiks.ui.snackbar

import android.util.Log
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SnackbarManager @Inject constructor() {

    init {
        Log.d(TAG, "SnackbarManager created")
    }

    private val messages: MutableStateFlow<SnackbarMessage?> = MutableStateFlow(null)
    val snackbarMessages: StateFlow<SnackbarMessage?>
        get() = messages.asStateFlow()

    fun showMessage(@StringRes message: Int) {
        messages.value = SnackbarMessage.ResourceSnackbar(message)
    }

    fun showMessage(@StringRes message: Int, vararg  formatArgs: Any) {
        messages.value = SnackbarMessage.FormattedSnackbar(message, *formatArgs)
    }

    fun showMessage(@PluralsRes message: Int, count: Int, vararg formatArgs: Any) {
        messages.value =
            SnackbarMessage.PluralsSnackbar(message, count, *formatArgs)
    }

    fun showMessage(message: SnackbarMessage) {
        messages.value = message
    }

    companion object {
        private const val TAG = "SnackbarManager"
    }
}
package com.folkmanis.laiks.ui.snackbar

import android.content.res.Resources
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import com.folkmanis.laiks.R

sealed class SnackbarMessage {

    class StringSnackbar(val message: String) : SnackbarMessage()
    class ResourceSnackbar(@StringRes val message: Int) : SnackbarMessage()

    class FormattedSnackbar(
        @StringRes val message: Int,
        vararg val formatArgs: Any
    ): SnackbarMessage()
    class PluralsSnackbar(
        @PluralsRes val message: Int,
        val count: Int,
        vararg val formatArgs: Any
    ) : SnackbarMessage()

    companion object {
        fun SnackbarMessage.toMessage(resources: Resources): String {
            return when (this) {
                is StringSnackbar -> message
                is ResourceSnackbar -> resources.getString(message)
                is PluralsSnackbar -> resources.getQuantityString(message, count, *formatArgs)
                is FormattedSnackbar -> resources.getString(message, *formatArgs)
            }
        }

        fun Throwable.toSnackbarMessage(): SnackbarMessage {
            val message = this.message.orEmpty()
            return if (message.isNotBlank()) StringSnackbar(message)
            else ResourceSnackbar(R.string.generic_error)
        }
    }
}
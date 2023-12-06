package com.folkmanis.laiks.ui.screens.login.password_reset

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.ui.snackbar.SnackbarManager
import com.folkmanis.laiks.ui.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.folkmanis.laiks.utilities.ext.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordResetViewModel @Inject constructor(
    private val accountService: AccountService,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {

    var uiState by mutableStateOf(PasswordResetUiState())
        private set

    var busy
        get() = uiState.isBusy
        set(value) {
            uiState.copy(isBusy = value)
        }

    fun setEmail(newEmail: String) {
        uiState = uiState.copy(email = newEmail)
    }

    fun resetPassword(onReset: () -> Unit) {

        val email = uiState.email

        if (!email.isValidEmail()) {
            snackbarManager.showMessage(R.string.email_input_error)
            return
        }

        busy = true

        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                snackbarManager.showMessage(throwable.toSnackbarMessage())
                busy = false
            }
        ) {
            accountService.resetPassword(email)
            snackbarManager.showMessage(R.string.password_reset_email_sent, email)
            busy = false
        }
    }
}
package com.folkmanis.laiks.ui.screens.password_reset

import androidx.compose.runtime.mutableStateOf
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
):ViewModel() {

    var state = mutableStateOf(PasswordResetUiState())
        private set
    private val email: String
        get() = state.value.email

    fun setEmail(newEmail: String) {
        state.value = state.value.copy(email = newEmail)
    }
    fun resetPassword(onPasswordResetSent: ()->Unit) {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                snackbarManager.showMessage(throwable.toSnackbarMessage())
            }
        ) {
            if(!email.isValidEmail()) {
                snackbarManager.showMessage(R.string.email_input_error)
                return@launch
            }
            accountService.resetPassword(email)
            snackbarManager.showMessage(R.string.password_reset_email_sent, email)
            onPasswordResetSent()
        }
    }
}
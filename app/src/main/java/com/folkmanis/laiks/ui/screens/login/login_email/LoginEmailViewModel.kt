package com.folkmanis.laiks.ui.screens.login.login_email

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
import com.folkmanis.laiks.utilities.ext.isValidPassword
import com.google.firebase.auth.EmailAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginEmailViewModel @Inject constructor(
    private val snackbarManager: SnackbarManager,
    private val accountService: AccountService,
) : ViewModel() {

    var uiState by mutableStateOf(LoginEmailUiState())
        private set

    private val email
        get() = uiState.email
    private val password
        get() = uiState.password

    private var isBusy
        get() = uiState.isBusy
        set(value) {
            uiState = uiState.copy(isBusy = value)
        }

    fun setEmail(newEmail: String) {
        uiState = uiState.copy(email = newEmail)
    }

    fun setPassword(newPassword: String) {
        if (newPassword.isEmpty() || newPassword.isValidPassword())
            uiState = uiState.copy(password = newPassword)
    }

    fun loginWithEmail(afterLogin: () -> Unit) {
        if (!email.isValidEmail()) {
            snackbarManager.showMessage(R.string.email_input_error)
            return
        }
        if (password.isEmpty()) {
            snackbarManager.showMessage(R.string.password_input_error)
            return
        }

        isBusy = true

        val credential = EmailAuthProvider.getCredential(email, password)

        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                snackbarManager.showMessage(throwable.toSnackbarMessage())
                isBusy = false
            }
        ) {
            val result = accountService.loginWithCredential(credential)
            result.user?.also { user ->
                if (!user.isEmailVerified) {
                    snackbarManager.showMessage(R.string.email_not_verified)
                }
                afterLogin()
            }
            isBusy = false
        }
    }

}
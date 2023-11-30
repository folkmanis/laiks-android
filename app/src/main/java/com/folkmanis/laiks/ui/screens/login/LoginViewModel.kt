package com.folkmanis.laiks.ui.screens.login

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.ui.snackbar.SnackbarManager
import com.folkmanis.laiks.ui.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.folkmanis.laiks.utilities.ext.isValidEmail
import com.folkmanis.laiks.utilities.ext.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountService: AccountService,
    private val laiksUserService: LaiksUserService,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {

    var uiState = mutableStateOf(LoginUiState())
        private set

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password

    private var isBusy
        get() = uiState.value.isBusy
        set(value) {
            uiState.value = uiState.value.copy(isBusy = value)
        }

    fun setEmail(newEmail: String) {
        uiState.value = uiState.value.copy(email = newEmail)
    }

    fun setPassword(newPassword: String) {
        if (newPassword.isEmpty() || newPassword.isValidPassword())
            uiState.value = uiState.value.copy(password = newPassword)
    }

    fun logout(context: Context) {
        accountService.authUser?.also {
            AuthUI.getInstance()
                .signOut(context)
            Log.d(TAG, "Logged out")
        }
    }

    fun onLoginResult(
        result: FirebaseAuthUIAuthenticationResult,
        afterLogin: () -> Unit,
        onLaiksUserCreated: () -> Unit
    ) {
        if (result.resultCode == Activity.RESULT_OK) {
            loginSuccess(afterLogin, onLaiksUserCreated)
        }
    }

    fun loginWithEmail(afterLogin: () -> Unit, onLaiksUserCreated: () -> Unit) {
        if (!email.isValidEmail()) {
            snackbarManager.showMessage(R.string.email_input_error)
            return
        }
        if (password.isEmpty()) {
            snackbarManager.showMessage(R.string.password_input_error)
            return
        }

        isBusy = true

        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                snackbarManager.showMessage(throwable.toSnackbarMessage())
                isBusy = false
            }
        ) {
            accountService.loginWithEmail(email, password)
            accountService.authUser?.also { user ->
                if (!user.isEmailVerified) {
                    snackbarManager.showMessage(R.string.email_not_verified)
                }
                loginSuccess(afterLogin, onLaiksUserCreated)
            }
            isBusy = false
        }
    }

    private fun loginSuccess(afterLogin: () -> Unit, onLaiksUserCreated: () -> Unit) {
        viewModelScope.launch {
            val user = accountService.authUser
            snackbarManager.showMessage(R.string.login_success)
            if (user != null && !laiksUserService.userExists(user.uid)) {
                laiksUserService.createLaiksUser(user)
                onLaiksUserCreated()
            } else {
                afterLogin()
            }
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }

}
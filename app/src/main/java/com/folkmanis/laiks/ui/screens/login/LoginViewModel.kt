package com.folkmanis.laiks.ui.screens.login

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    var uiState by mutableStateOf(LoginUiState())
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

    fun loginWithGoogle(tokenId: String, afterLogin: () -> Unit, onLaiksUserCreated: () -> Unit) {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                snackbarManager.showMessage(throwable.toSnackbarMessage())
                isBusy = false
            }
        ) {
            accountService.loginWithGoogle(tokenId)
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
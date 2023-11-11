package com.folkmanis.laiks.ui.screens.user_registration

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.ui.snackbar.SnackbarManager
import com.folkmanis.laiks.ui.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.folkmanis.laiks.utilities.ext.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserRegistrationViewModel @Inject constructor(
    private val accountService: AccountService,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {

    var uiState = mutableStateOf(UserRegistrationUiState())
        private set

    private var busy: Boolean
        get() = uiState.value.isBusy
        set(value) {
            uiState.value = uiState.value.copy(isBusy = value)
        }
    private val email
        get() = uiState.value.email

    private val password
        get() = uiState.value.password

    private val displayName
        get() = uiState.value.displayName.trim()

    fun setEmail(newEmail: String) {
        uiState.value = uiState.value.copy(email = newEmail, emailPristine = false)
    }

    fun setPassword(newPassword: String) {
        if (newPassword.isEmpty() || newPassword.isValidPassword())
            uiState.value = uiState.value.copy(password = newPassword, passwordPristine = false)
    }

    fun setPasswordRepeat(newPassword: String) {
        if (newPassword.isEmpty() || newPassword.isValidPassword())
            uiState.value = uiState.value.copy(passwordRepeat = newPassword, passwordRepeatPristine = false)
    }

    fun setDisplayName(newName: String) {
        uiState.value = uiState.value.copy(displayName = newName, displayNamePristine = false)
    }

    fun createUser(onSuccess: () -> Unit) {
        if (!uiState.value.isValid) {
            return
        }
        busy = true
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                snackbarManager.showMessage(throwable.toSnackbarMessage())
                busy = false
            }
        ) {
            accountService.createUserWithEmail(email, password, displayName)
            accountService.authUser?.also { user ->
                snackbarManager.showMessage(R.string.user_registered,user.email!!)
                accountService.signOut()
                onSuccess()
            }
            busy = false
        }
    }

    companion object {
        @Suppress("unused")
        private const val TAG = "UserRegistrationViewModel"
    }
}
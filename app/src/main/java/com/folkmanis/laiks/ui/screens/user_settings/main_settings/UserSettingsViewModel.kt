package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.MarketZonesService
import com.folkmanis.laiks.model.LaiksUser
import com.folkmanis.laiks.model.MarketZone
import com.folkmanis.laiks.ui.snackbar.SnackbarManager
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserSettingsViewModel @Inject constructor(
    private val zonesService: MarketZonesService,
    private val laiksUserService: LaiksUserService,
    private val snackbarManager: SnackbarManager,
    private val accountService: AccountService,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserSettingsUiState())
    val uiState = _uiState.asStateFlow()

    private var loading: Boolean
        set(value) = _uiState.update { state ->
            state.copy(loading = value)
        }
        get() = _uiState.value.loading

    fun initialize() {

        viewModelScope.launch {

            laiksUserService.laiksUserFlow()
                .filterNotNull()
                .collect { user ->
                    updateStateWithUser(user)
                }

        }

        viewModelScope.launch {

            laiksUserService.npAllowedFlow.collect { npAllowed ->
                _uiState.update { state ->
                    state.copy(npUser = npAllowed)
                }
            }

        }

        viewModelScope.launch {
            accountService.firebaseUserFlow
                .map { user ->
                    user?.isEmailVerified ?: false
                }
                .collect { emailVerified ->
                    _uiState.update { state ->
                        state.copy(emailVerified = emailVerified)
                    }
                }
        }
    }

    fun setName(value: String) {
        _uiState.update { state ->
            state.id?.let {
                viewModelScope.launch {
                    laiksUserService.updateLaiksUser("name", value)
                }
                state.copy(name = value)
            } ?: state
        }
    }

    fun setIncludeVat(value: Boolean) {
        _uiState.update { state ->
            state.id?.let {
                viewModelScope.launch {
                    laiksUserService.updateLaiksUser("includeVat", value)
                }
                state.copy(includeVat = value)
            } ?: state
        }
    }

    fun setVatAmount(value: Double) {
        _uiState.update { state ->
            state.id?.let {
                viewModelScope.launch {
                    laiksUserService.updateLaiksUser(
                        "vatAmount", value
                    )
                }
                state.copy(vatAmount = value)
            } ?: state
        }
    }

    fun setMarketZoneId(value: MarketZone) {
        _uiState.update { state ->
            state.id?.let {
                viewModelScope.launch {
                    laiksUserService.updateLaiksUser(
                        hashMapOf(
                            "marketZoneId" to value.id,
                            "vatAmount" to value.tax,
                        )
                    )
                }
                state.copy(
                    marketZoneId = value.id,
                    marketZoneName = "${value.id}, ${value.description}",
                    vatAmount = value.tax,
                )
            } ?: state
        }
    }

    fun deleteAccount(onDeleted: () -> Unit) {

        loading = true

        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            if (throwable is FirebaseAuthRecentLoginRequiredException) {
                Log.d(TAG, "Must re-login")
                _uiState.update { state ->
                    state.copy(userToReAuthenticateAndDelete = accountService.authUser)
                }
            } else throw throwable
        }) {

            accountService.deleteAccount()
            Log.d(TAG, "User ${uiState.value.email} deleted")
            snackbarManager
                .showMessage(R.string.user_deleted_success, uiState.value.email)
            _uiState
                .update { state -> state.copy(userToReAuthenticateAndDelete = null) }
            loading = false
            onDeleted()
        }
    }

    fun cancelReLogin() {
        loading = false
        _uiState.update { state ->
            state.copy(userToReAuthenticateAndDelete = null)
        }
        snackbarManager.showMessage(R.string.user_not_deleted, uiState.value.email)
    }

    fun sendEmailVerification() {
        val email = _uiState.value.email
        viewModelScope.launch {
            accountService.sendEmailVerification()
            snackbarManager.showMessage(R.string.email_verification_sent, email)
        }
    }

    private suspend fun updateStateWithUser(user: LaiksUser) {

        val zone = zonesService.getMarketZone(user.marketZoneId)

        _uiState.update { state ->
            state.copy(
                loading = false,
                id = user.id,
                email = user.email,
                includeVat = user.includeVat,
                name = user.name,
                marketZoneId = user.marketZoneId,
                vatAmount = user.vatAmount,
                marketZoneName = "${user.marketZoneId}, ${zone?.description ?: ""}",
            )
        }
    }

    companion object {
        private const val TAG = "UserSettingsViewModel"
    }
}
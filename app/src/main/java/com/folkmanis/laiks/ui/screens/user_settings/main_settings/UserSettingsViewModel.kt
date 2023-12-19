package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.MarketZonesService
import com.folkmanis.laiks.data.PermissionsService
import com.folkmanis.laiks.data.domain.NpBlockedUseCase
import com.folkmanis.laiks.model.LaiksUser
import com.folkmanis.laiks.model.MarketZone
import com.folkmanis.laiks.ui.snackbar.SnackbarManager
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

fun UserSettingsUiState.successOrNull(): UserSettingsUiState.Success? {
    return if (this is UserSettingsUiState.Success) this else null
}

fun UserSettingsUiState.ifSuccess(action: (UserSettingsUiState.Success) -> Unit) {
    if (this is UserSettingsUiState.Success) action(this)
}

@HiltViewModel
class UserSettingsViewModel @Inject constructor(
    private val zonesService: MarketZonesService,
    private val laiksUserService: LaiksUserService,
    private val snackbarManager: SnackbarManager,
    private val accountService: AccountService,
    private val npBlocked: NpBlockedUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserSettingsUiState>(UserSettingsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var busy: Boolean
        set(value) = _uiState.update { state ->
            state.successOrNull()?.copy(loading = value) ?: state
        }
        get() = _uiState.value.successOrNull()?.loading ?: false

    fun initialize() {
        viewModelScope.launch {
            combine(
                laiksUserService.laiksUserFlow(),
                accountService.firebaseUserFlow,
                npBlocked()
            ) { laiksUser, user, isNpBlocked ->
                if (laiksUser != null && user != null) {
                    settingsUiState(laiksUser, user, isNpBlocked)
                } else
                    UserSettingsUiState.Loading
            }
                .collect { state ->
                    _uiState.value = state
                }
        }
    }

    private suspend fun settingsUiState(
        laiksUser: LaiksUser,
        user: FirebaseUser,
        npBlocked: Boolean,
    ): UserSettingsUiState {

        val marketZoneId = laiksUser.marketZoneId
        val zone =
            if (marketZoneId != null) zonesService.getMarketZone(marketZoneId)
            else null
        val marketZoneName = zone?.let { "${it.id}, ${it.description}" } ?: ""

        return UserSettingsUiState.Success(

            loading = false,
            id = laiksUser.id,
            email = laiksUser.email,
            includeVat = laiksUser.includeVat,
            name = laiksUser.name,
            marketZoneId = marketZoneId,
            vatAmount = laiksUser.vatAmount,
            marketZoneName = marketZoneName,

            npAllowed = !npBlocked,
            anonymousUser = user.isAnonymous,
            emailVerified = user.isEmailVerified,

            )

    }

    fun setName(value: String) {
        _uiState.update { state ->
            state.successOrNull()?.let {
                viewModelScope.launch {
                    laiksUserService.updateLaiksUser("name", value)
                }
                it.copy(name = value)
            } ?: state
        }
    }

    fun setIncludeVat(value: Boolean) {
        _uiState.update { state ->
            state.successOrNull()?.let {
                viewModelScope.launch {
                    laiksUserService.updateLaiksUser("includeVat", value)
                }
                it.copy(includeVat = value)
            } ?: state
        }
    }

    fun setVatAmount(value: Double) {
        _uiState.update { state ->
            state.successOrNull()?.let {
                viewModelScope.launch {
                    laiksUserService.updateLaiksUser(
                        "vatAmount", value
                    )
                }
                it.copy(vatAmount = value)
            } ?: state
        }
    }

    fun setMarketZoneId(value: MarketZone) {
        _uiState.update { state ->
            state.successOrNull()?.let {
                viewModelScope.launch {
                    laiksUserService.updateLaiksUser(
                        hashMapOf(
                            "marketZoneId" to value.id,
                            "vatAmount" to value.tax,
                        )
                    )
                }
                it.copy(
                    marketZoneId = value.id,
                    marketZoneName = "${value.id}, ${value.description}",
                    vatAmount = value.tax,
                )
            } ?: state
        }
    }

    fun deleteAccount(onDeleted: () -> Unit) {

        busy = true

        val state = _uiState.value

        if (state is UserSettingsUiState.Success)
            viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
                if (throwable is FirebaseAuthRecentLoginRequiredException) {
                    Log.d(TAG, "Must re-login")
                    _uiState.value =
                        state.copy(userToReAuthenticateAndDelete = accountService.authUser)
                } else throw throwable
            }) {

                accountService.deleteAccount()
                Log.d(TAG, "User ${state.email} deleted")
                snackbarManager
                    .showMessage(R.string.user_deleted_success, state.email)

                _uiState.value = state.copy(userToReAuthenticateAndDelete = null)

                busy = false

                onDeleted()
            }
    }

    fun cancelReLogin() {
        busy = false
        _uiState.update { state ->
            if (state is UserSettingsUiState.Success) {
                snackbarManager.showMessage(R.string.user_not_deleted, state.email)
                state.copy(userToReAuthenticateAndDelete = null)
            } else state
        }
    }

    fun sendEmailVerification() {
        _uiState.value.ifSuccess { state ->
            viewModelScope.launch {
                accountService.sendEmailVerification()
                snackbarManager.showMessage(R.string.email_verification_sent, state.email)
            }
        }
    }


    companion object {
        private const val TAG = "UserSettingsViewModel"
    }
}
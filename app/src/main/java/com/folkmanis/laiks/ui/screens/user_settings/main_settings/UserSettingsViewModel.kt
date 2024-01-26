package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.MarketZonesService
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

fun UserSettingsUiState.successOrNull(): UserSettingsUiState.Success? {
    return if (this is UserSettingsUiState.Success) this else null
}

inline fun MutableStateFlow<UserSettingsUiState>.updateSuccess(action: (UserSettingsUiState.Success) -> UserSettingsUiState.Success) {
    this.update { state ->
        if (state is UserSettingsUiState.Success) {
            action(state)
        } else state
    }
}

@HiltViewModel
class UserSettingsViewModel @Inject constructor(
    private val zonesService: MarketZonesService,
    private val laiksUserService: LaiksUserService,
    private val snackbarManager: SnackbarManager,
    private val accountService: AccountService,
    private val npBlocked: NpBlockedUseCase,
   private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {

    private val _uiState = MutableStateFlow<UserSettingsUiState>(UserSettingsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _nextRoute =
        savedStateHandle.getStateFlow<String?>(NEXT_ROUTE_ON_ZONE_SET,null)
    private var busy: Boolean
        set(value) = _uiState.updateSuccess { it.copy(busy = value) }
        get() = _uiState.value.successOrNull()?.busy ?: false

    fun initialize() {
        Log.d(TAG, "${savedStateHandle.get<String?>(NEXT_ROUTE_ON_ZONE_SET)}")
        viewModelScope.launch {
            combine(
                laiksUserService.laiksUserFlow(),
                accountService.firebaseUserFlow,
                npBlocked(),
                _nextRoute,
            ) { laiksUser, user, isNpBlocked, nextRoute ->
                if (laiksUser != null && user != null) {
                    settingsUiState(
                        laiksUser,
                        user,
                        isNpBlocked,
                        nextRoute,
                    )
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
        nextRoute: String?,
    ): UserSettingsUiState {

        val marketZoneId = laiksUser.marketZoneId
        val zone =
            if (marketZoneId != null) zonesService.getMarketZone(marketZoneId)
            else null
        val marketZoneName = zone?.let { "${it.id}, ${it.description}" } ?: ""

        return UserSettingsUiState.Success(

            busy = false,
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
            nextRoute = nextRoute,
            marketZoneEditOpen = nextRoute != null,
        )

    }

    fun setName(value: String) {
        busy = true
        _uiState.updateSuccess { state ->
            viewModelScope.launch {
                laiksUserService.updateLaiksUser("name", value)
                busy = false
            }
            state.copy(name = value)
        }
    }

    fun setIncludeVat(value: Boolean) {
        busy = true
        _uiState.updateSuccess { state ->
            viewModelScope.launch {
                laiksUserService.updateLaiksUser("includeVat", value)
                busy = false
            }
            state.copy(includeVat = value)
        }
    }

    fun setVatAmount(value: Double) {
        busy = true
        _uiState.updateSuccess { state ->
            viewModelScope.launch {
                laiksUserService.updateLaiksUser(
                    "vatAmount", value
                )
                busy = false
            }
            state.copy(vatAmount = value)
        }
    }

    fun setMarketZoneEditState(isOpen: Boolean) {
        _uiState.updateSuccess { state ->
            state.copy(marketZoneEditOpen = isOpen)
        }
    }

    fun setMarketZoneId(
        value: MarketZone?,
        onMarketZoneSet: (String) -> Unit,
        onMarketZoneNotSet: () -> Unit,
    ) {
        busy = true
        _uiState.updateSuccess { state ->
            if (value == null) {
                if (state.shouldSetZone)
                    onMarketZoneNotSet()
                state.copy(
                    marketZoneEditOpen = false,
                    busy = false
                )
            } else {
                viewModelScope.launch {
                    laiksUserService.updateLaiksUser(
                        hashMapOf(
                            "marketZoneId" to value.id,
                            "vatAmount" to value.tax,
                        )
                    )
                    busy = false
                    if (state.nextRoute != null) {
                        onMarketZoneSet(state.nextRoute)
                    }
                }
                state.copy(
                    marketZoneId = value.id,
                    marketZoneName = "${value.id}, ${value.description}",
                    vatAmount = value.tax,
                    marketZoneEditOpen = false,
                )
            }
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
        _uiState.value.also { state ->
            if (state is UserSettingsUiState.Success) {
                viewModelScope.launch {
                    accountService.sendEmailVerification()
                    snackbarManager.showMessage(R.string.email_verification_sent, state.email)
                }
            }
        }
    }


    companion object {
        private const val TAG = "UserSettingsViewModel"
    }
}
package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.AuthUI
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.MarketZonesService
import com.folkmanis.laiks.data.PermissionsService
import com.folkmanis.laiks.data.implementations.LaiksUserServiceFirebase
import com.folkmanis.laiks.model.LaiksUser
import com.folkmanis.laiks.model.MarketZone
import com.folkmanis.laiks.ui.snackbar.SnackbarManager
import com.folkmanis.laiks.ui.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class UserSettingsViewModel @Inject constructor(
    private val zonesService: MarketZonesService,
    private val laiksUserService: LaiksUserService,
    private val permissionsService: PermissionsService,
    private val snackbarManager: SnackbarManager,
    private val accountService: AccountService,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserSettingsUiState())
    val uiState = _uiState.asStateFlow()

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
                Log.d(TAG, "NpUser: $npAllowed")
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
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            if (throwable is FirebaseAuthRecentLoginRequiredException) {
                Log.d(TAG, "Must relogin")
                _uiState.update { state -> state.copy(shouldReAuthenticateAndDelete = true) }
            }
        }) {

            val laiksUserName = laiksUserService.laiksUser().name
            accountService.deleteAccount()
            Log.d(TAG, "User $laiksUserName deleted")
            snackbarManager.showMessage(R.string.user_deleted_success, laiksUserName)
            onDeleted()
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
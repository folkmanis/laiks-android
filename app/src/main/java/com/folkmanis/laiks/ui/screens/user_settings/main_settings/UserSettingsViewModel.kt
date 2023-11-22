package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.AuthUI
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.MarketZonesService
import com.folkmanis.laiks.data.PermissionsService
import com.folkmanis.laiks.data.implementations.LaiksUserServiceFirebase
import com.folkmanis.laiks.model.LaiksUser
import com.folkmanis.laiks.model.MarketZone
import com.folkmanis.laiks.ui.snackbar.SnackbarManager
import com.folkmanis.laiks.ui.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class UserSettingsViewModel @Inject constructor(
    private val zonesService: MarketZonesService,
    private val laiksUserService: LaiksUserService,
    private val snackbarManager: SnackbarManager,
    private val permissionsService: PermissionsService,
    private val accountService: AccountService,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserSettingsUiState>(UserSettingsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun initialize() {

        viewModelScope.launch {
            laiksUserService.laiksUserFlow()
                .flatMapLatest { user -> toUiStateFlow(user) }
                .catch { err -> logMissingUser(err) }
                .collect { _uiState.value = it }
        }
    }

    fun setName(value: String) {
        val state = _uiState.value
        if (state is UserSettingsUiState.Success && value.isNotEmpty()) {
            _uiState.value = state.copy(name = value)
            viewModelScope.launch {
                laiksUserService.updateLaiksUser("name", value)
            }
        }
    }

    fun setIncludeVat(value: Boolean) {
        val state = _uiState.value
        if (state is UserSettingsUiState.Success) {
            _uiState.value = state.copy(includeVat = value)
            viewModelScope.launch {
                laiksUserService.updateLaiksUser("includeVat", value)
            }
        }
    }

    fun setVatAmount(value: Double) {
        val state = _uiState.value
        if (state is UserSettingsUiState.Success) {
            _uiState.value = state.copy(vatAmount = value)
            viewModelScope.launch {
                laiksUserService.updateLaiksUser(
                    "vatAmount", value
                )
            }
        }
    }

    fun setMarketZoneId(value: MarketZone) {
        val state = _uiState.value
        if (state is UserSettingsUiState.Success) {
            _uiState.value = state.copy(
                marketZoneId = value.id,
                marketZoneName = "${value.id}, ${value.description}",
                vatAmount = value.tax,
            )
            val update = hashMapOf<String, Any>(
                "marketZoneId" to value.id,
                "vatAmount" to value.tax,
            )
            viewModelScope.launch {
                laiksUserService.updateLaiksUser(
                    update
                )
            }
        }
    }

    fun deleteAccount(context: Context, onDeleted: () -> Unit) {

        viewModelScope.launch {
            val user = accountService.authUser!!
            val token = user.getIdToken(true).await()
            Log.d(TAG, "Provider: ${token?.signInProvider}")
            if (token.signInProvider == GoogleAuthProvider.PROVIDER_ID) {
                AuthUI.getInstance().delete(context).await()
                onDeleted()

//                val credential = GoogleAuthProvider.getCredential(token.token, null)
//                Log.d(TAG, "Credential: ${credential}")
//                val result = user.reauthenticate(credential).await()
//                Log.d(TAG, "Google result: ${result}")
            }

//            laiksUserService.deleteLaiksUser()
        }
    }

    private fun toUiStateFlow(user: LaiksUser?): Flow<UserSettingsUiState> {
        if (user == null) {
            throw NoSuchElementException()
        }
        return permissionsService.npBlockedFlow(user.id)
            .map { npBlocked ->
                val zone = zonesService.getMarketZone(user.marketZoneId)
                UserSettingsUiState.Success(
                    id = user.id,
                    email = user.email,
                    includeVat = user.includeVat,
                    name = user.name,
                    marketZoneId = user.marketZoneId,
                    vatAmount = user.vatAmount,
                    npUser = !npBlocked,
                    marketZoneName = "${user.marketZoneId}, ${zone?.description ?: ""}",
                )
            }
    }

    private fun logMissingUser(err: Throwable): UserSettingsUiState.Error {
        Log.e(TAG, err.message, err)
        snackbarManager.showMessage(err.toSnackbarMessage())
        return UserSettingsUiState.Error(err.message ?: "No user", err)
    }

    companion object {
        private const val TAG = "UserSettingsViewModel"
    }
}
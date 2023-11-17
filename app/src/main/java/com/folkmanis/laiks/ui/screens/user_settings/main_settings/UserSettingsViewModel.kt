package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.MarketZonesService
import com.folkmanis.laiks.data.PermissionsService
import com.folkmanis.laiks.model.LaiksUser
import com.folkmanis.laiks.model.MarketZone
import com.folkmanis.laiks.ui.snackbar.SnackbarManager
import com.folkmanis.laiks.ui.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserSettingsViewModel @Inject constructor(
    private val zonesService: MarketZonesService,
    private val laiksUserService: LaiksUserService,
    private val snackbarManager: SnackbarManager,
    private val permissionsService: PermissionsService,
) : ViewModel() {

    var uiState = MutableStateFlow<UserSettingsUiState>(UserSettingsUiState.Loading)
        private set

    fun initialize() {

        viewModelScope.launch {
            laiksUserService.laiksUserFlow()
                .map { user -> toUiState(user) }
                .catch { err -> logMissingUser(err) }
                .collect { uiState.value = it }
        }
    }

    fun setName(value: String) {
        val state = uiState.value
        if (state is UserSettingsUiState.Success && value.isNotEmpty()) {
            uiState.value = state.copy(name = value)
            viewModelScope.launch {
                laiksUserService.updateLaiksUser("name", value)
            }
        }
    }

    fun setIncludeVat(value: Boolean) {
        val state = uiState.value
        if (state is UserSettingsUiState.Success) {
            uiState.value = state.copy(includeVat = value)
            viewModelScope.launch {
                laiksUserService.updateLaiksUser("includeVat", value)
            }
        }
    }

    fun setVatAmount(value: Double) {
        val state = uiState.value
        if (state is UserSettingsUiState.Success) {
            uiState.value = state.copy(vatAmount = value)
            viewModelScope.launch {
                laiksUserService.updateLaiksUser(
                    "vatAmount", value
                )
            }
        }
    }

    fun setMarketZoneId(value: MarketZone) {
        val state = uiState.value
        if (state is UserSettingsUiState.Success) {
            uiState.value = state.copy(
                marketZoneId = value.id,
                marketZoneName = "${value.id}, ${value.description}",
                vatAmount = value.tax,
            )
            val update = hashMapOf<String,Any>(
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

    private suspend fun toUiState(user: LaiksUser?): UserSettingsUiState {
        if (user == null) {
            throw NoSuchElementException()
        }
        val zone = zonesService.getMarketZone(id = user.marketZoneId)
        val npBlocked =  permissionsService.getPermissions(user.id).npBlocked
        return UserSettingsUiState.Success(
            id = user.id,
            email = user.email,
//            appliances = user.appliances,
            includeVat = user.includeVat,
            name = user.name,
            marketZoneId = user.marketZoneId,
            vatAmount = user.vatAmount,
            npUser = !npBlocked,
            marketZoneName = "${user.marketZoneId}, ${zone?.description ?: ""}",
        )
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
package com.folkmanis.laiks.ui.screens.user_settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.LocalesService
import com.folkmanis.laiks.data.MarketZonesService
import com.folkmanis.laiks.data.PermissionsService
import com.folkmanis.laiks.model.LaiksUser
import com.folkmanis.laiks.ui.snackbar.SnackbarManager
import com.folkmanis.laiks.ui.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class UserSettingsViewModel @Inject constructor(
    private val zonesService: MarketZonesService,
    private val localesService: LocalesService,
    private val laiksUserService: LaiksUserService,
    private val snackbarManager: SnackbarManager,
    private val permissionsService: PermissionsService,
) : ViewModel() {

    val localesFlow = localesService.localesFlow
    val zonesFlow = zonesService.marketZonesFlow

    private val _uiState = MutableStateFlow<UserSettingsUiState>(UserSettingsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun initialize(id: String?) {

        if (id == null) return

        viewModelScope.launch {
            laiksUserService.laiksUserFlow(id)
                .map { user -> toUiState(user) }
                .catch { err -> logMissingUser(err, id) }
                .collect { _uiState.value = it }
        }
    }

    fun setIncludeVat(value: Boolean) {
        val state = uiState.value
        if (state is UserSettingsUiState.Success) {
            _uiState.value = state.copy(includeVat = value)
            viewModelScope.launch {
                laiksUserService.updateLaiksUser(state.id,"includeVat", value)
            }
        }
    }

    private suspend fun toUiState(user: LaiksUser?): UserSettingsUiState {
        if (user == null) {
            throw NoSuchElementException()
        }
        val localeInfo = localesService.getLocale(id = user.locale)
        val zone = zonesService.getMarketZone(id = user.marketZoneId)
        val npUser = permissionsService.getPermissions(user.id).npUser
        return UserSettingsUiState.Success(
            id = user.id,
            email = user.email,
            appliances = user.appliances,
            includeVat = user.includeVat,
            name = user.name,
            marketZoneId = user.marketZoneId,
            marketZoneName = zone?.description ?: "",
            locale = user.locale,
            localeName = localeInfo?.language ?: "",
            vatAmount = user.vatAmount,
            npUser = npUser,
        )
    }

    private fun logMissingUser(err: Throwable, id: String): UserSettingsUiState.Error {
        Log.e(TAG, err.message, err)
        snackbarManager.showMessage(err.toSnackbarMessage())
        return UserSettingsUiState.Error(err.message ?: "No user", err)
    }

    companion object {
        private const val TAG = "UserSettingsViewModel"
    }
}
package com.folkmanis.laiks.ui.screens.user_settings

import com.folkmanis.laiks.model.ApplianceRecord

sealed interface UserSettingsUiState {

    object Loading : UserSettingsUiState

    data class Error(
        val reason: String,
        val exception: Throwable,
    ) : UserSettingsUiState

    data class Success(
        val id: String,
        val email: String,
        val name: String,
        val appliances: List<ApplianceRecord>,
        val marketZoneId: String,
        val marketZoneName: String,
        val includeVat: Boolean,
        val vatAmount: Double,
        val locale: String,
        val localeName: String,
        val npUser: Boolean
    ) : UserSettingsUiState

}
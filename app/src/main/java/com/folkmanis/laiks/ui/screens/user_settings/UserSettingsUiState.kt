package com.folkmanis.laiks.ui.screens.user_settings

import com.folkmanis.laiks.data.fake.FakeAppliancesService
import com.folkmanis.laiks.data.fake.FakeLaiksUserService
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
        val includeVat: Boolean,
        val vatAmount: Double,
        val npUser: Boolean,
        val marketZoneName : String,
    ) : UserSettingsUiState

    companion object {
        private val user = FakeLaiksUserService.laiksUser
        val testUiState = Success(
            name = user.name,
            email = user.email,
            id = user.id,
            marketZoneId = user.marketZoneId,
            npUser = true,
            includeVat = user.includeVat,
            vatAmount = user.vatAmount,
            appliances = FakeAppliancesService.testApplianceRecords,
            marketZoneName = "LV, Latvija"
        )
    }

}
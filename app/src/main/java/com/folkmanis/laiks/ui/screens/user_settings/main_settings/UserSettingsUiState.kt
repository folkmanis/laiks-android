package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import com.folkmanis.laiks.data.fake.FakeLaiksUserService
import com.google.firebase.auth.FirebaseUser

sealed interface UserSettingsUiState {

    data object Loading : UserSettingsUiState
    data class Success(
        val loading: Boolean = true,
        val id: String = "",
        val email: String = "",
        val emailVerified: Boolean = false,
        val name: String = "",
        val marketZoneId: String? = null,
        val includeVat: Boolean = true,
        val vatAmount: Double? = null,
        val npAllowed: Boolean = false,
        val marketZoneName: String = "",
        val userToReAuthenticateAndDelete: FirebaseUser? = null,
        val anonymousUser:Boolean = true,
        val marketZoneEditOpen: Boolean = false,
        val shouldSetZone: Boolean = false,
        val nextRoute: String? = null,
    ) : UserSettingsUiState


        companion object {
            private val user = FakeLaiksUserService.laiksUser
            val testUiState = Success(
                loading = false,
                name = user.name,
                email = user.email,
                id = user.id,
                marketZoneId = user.marketZoneId ?: "LV",
                npAllowed = true,
                includeVat = user.includeVat,
                vatAmount = user.vatAmount ?: 0.21,
                marketZoneName = "LV, Latvija"
            )
        }

}

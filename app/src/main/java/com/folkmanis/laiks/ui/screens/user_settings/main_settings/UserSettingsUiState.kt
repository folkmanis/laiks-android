package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import com.folkmanis.laiks.data.fake.FakeLaiksUserService
import com.google.firebase.auth.FirebaseUser

data class UserSettingsUiState(
    val loading: Boolean = true,
    val id: String? = null,
    val email: String = "",
    val emailVerified: Boolean = false,
    val name: String = "",
    val marketZoneId: String = "LV",
    val includeVat: Boolean = true,
    val vatAmount: Double = 0.0,
    val npUser: Boolean = false,
    val marketZoneName: String = "",
    val userToReAuthenticateAndDelete: FirebaseUser? = null,
) {
    companion object {
        private val user = FakeLaiksUserService.laiksUser
        val testUiState = UserSettingsUiState(
            loading = false,
            name = user.name,
            email = user.email,
            id = user.id,
            marketZoneId = user.marketZoneId,
            npUser = true,
            includeVat = user.includeVat,
            vatAmount = user.vatAmount,
            marketZoneName = "LV, Latvija"
        )
    }

}
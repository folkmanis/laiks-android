package com.folkmanis.laiks.ui.screens.user_menu

import android.net.Uri

sealed interface UserMenuUiState {
    data object NotLoggedIn : UserMenuUiState
    data class LoggedIn(
        val isAdmin: Boolean = false,
        val isPricesAllowed: Boolean = false,
        val displayName: String = "",
        val photoUrl: Uri? = null,
        val includeVat: Boolean = false,
        val userId: String = "",
    ) : UserMenuUiState

}
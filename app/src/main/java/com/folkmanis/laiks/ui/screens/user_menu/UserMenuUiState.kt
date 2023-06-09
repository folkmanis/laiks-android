package com.folkmanis.laiks.ui.screens.user_menu

import android.net.Uri

sealed interface UserMenuUiState {
    object NotLoggedIn : UserMenuUiState
    data class LoggedIn(
        val isAdmin: Boolean = false,
        val isPricesAllowed: Boolean = false,
        val isNpUploadAllowed: Boolean = false,
        val displayName: String = "",
        val photoUrl: Uri? = null,
    ) : UserMenuUiState

}
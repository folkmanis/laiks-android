package com.folkmanis.laiks.ui.screens.laiks

import android.net.Uri

sealed interface LaiksUiState {
    object NotLoggedIn : LaiksUiState
    data class LoggedIn(
        val isAdmin: Boolean = false,
        val isPricesAllowed: Boolean = false,
        val displayName: String = "",
        val photoUrl: Uri? = null,
    ) : LaiksUiState
}


package com.folkmanis.laiks.ui.screens.laiks

import com.google.firebase.auth.FirebaseUser

sealed interface LaiksUiState {
    object NotLoggedIn : LaiksUiState
    data class LoggedIn(
        val firebaseUser: FirebaseUser,
        val isAdmin: Boolean = false,
        val isPricesAllowed: Boolean = false,
    ) : LaiksUiState
}


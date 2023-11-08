package com.folkmanis.laiks.ui.screens.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isBusy: Boolean = false,
)

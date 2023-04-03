package com.folkmanis.laiks.ui.screens.users

import com.folkmanis.laiks.model.LaiksUser

sealed interface UsersUiState {

    data class Success(
        val users: List<LaiksUser>
    ) : UsersUiState

    object Loading : UsersUiState

    data class Error(
        val reason: String,
        val exception: Throwable,
    ) : UsersUiState
}

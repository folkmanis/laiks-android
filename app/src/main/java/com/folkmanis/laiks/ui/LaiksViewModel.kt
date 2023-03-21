package com.folkmanis.laiks.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.folkmanis.laiks.LaiksApplication
import com.folkmanis.laiks.data.AccountService
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class LaiksUiState(
    val user: FirebaseUser? = null,
)

class LaiksViewModel(
    accountService: AccountService
) : ViewModel() {

    val uiState = accountService.currentUser
        .map { user -> LaiksUiState(user) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            LaiksUiState(),
        )

    companion object {
        val Factory: ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val application =
                        (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LaiksApplication)
                    LaiksViewModel(
                        application.accountService,
                    )
                }
            }
    }


}
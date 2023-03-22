package com.folkmanis.laiks.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.folkmanis.laiks.LaiksApplication
import com.folkmanis.laiks.data.AccountService
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.*

sealed interface LaiksUiState {
    object NotLoggedIn : LaiksUiState
    data class LoggedIn(
        val firebaseUser: FirebaseUser,
        val isAdmin: Boolean = false,
        val isPricesAllowed: Boolean = false,
    ) : LaiksUiState
}

class LaiksViewModel(
    accountService: AccountService
) : ViewModel() {

    val uiState: StateFlow<LaiksUiState> = accountService.currentUser
        .flatMapLatest { user ->
            if (user == null) {
                flowOf(LaiksUiState.NotLoggedIn)
            } else {
                Log.d(TAG, "User Id ${user.uid} logged in")
                accountService.getLaiksUser(user.uid)
                    .map { laiksUser ->
                        LaiksUiState.LoggedIn(
                            firebaseUser = user,
                            isAdmin = laiksUser?.isAdmin ?: false,
                            isPricesAllowed = laiksUser?.npAllowed ?: false
                        )
                    }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            LaiksUiState.NotLoggedIn,
        )

    companion object {
        private const val TAG = "Laiks View Model"
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
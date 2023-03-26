package com.folkmanis.laiks.ui.screens.laiks

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.folkmanis.laiks.LaiksApplication
import com.folkmanis.laiks.data.AccountService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class LaiksViewModel(
    accountService: AccountService
) : ViewModel() {

    val uiState: StateFlow<LaiksUiState> = accountService.currentUser
        .flatMapLatest { user ->
            Log.d(TAG, "User: $user")
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
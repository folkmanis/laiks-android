package com.folkmanis.laiks.ui.screens.laiks

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.AuthUI
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class LaiksViewModel @Inject constructor(
    private val accountService: AccountService,
    private val settingsService: UserPreferencesRepository,
) : ViewModel() {

    val isVat = settingsService.includeVat

    fun setVat(value: Boolean) {
        viewModelScope.launch {
            settingsService.setIncludeVat(value)
        }
    }

    val uiState: StateFlow<LaiksUiState> = accountService.firebaseUserFlow
        .flatMapLatest { user ->
            Log.d(TAG, "User: $user")
            if (user == null) {
                flowOf(LaiksUiState.NotLoggedIn)
            } else {
                Log.d(TAG, "User Id ${user.uid} logged in")
                accountService.laiksUserFlow(user.uid)
                    .map { laiksUser ->
                        LaiksUiState.LoggedIn(
                            isAdmin = laiksUser?.isAdmin ?: false,
                            isPricesAllowed = laiksUser?.npAllowed ?: false,
                            displayName = user.displayName ?: "",
                            photoUrl = user.photoUrl
                        )
                    }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            LaiksUiState.NotLoggedIn,
        )

    fun login() {
        viewModelScope.launch {
            val user = accountService.authUser
            if (user != null && !accountService.userExists(user.uid)) {
                accountService.createLaiksUser(user)
            }
        }
    }

    fun logout(context: Context) {
        AuthUI.getInstance()
            .signOut(context)
    }

    companion object {
        private const val TAG = "Laiks View Model"
    }


}
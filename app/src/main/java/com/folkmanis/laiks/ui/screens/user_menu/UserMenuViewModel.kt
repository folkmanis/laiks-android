package com.folkmanis.laiks.ui.screens.user_menu

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
class UserMenuViewModel @Inject constructor(
    private val accountService: AccountService,
    private val settingsService: UserPreferencesRepository,
) : ViewModel() {

    val isVat = settingsService.includeVat

    fun setVat(value: Boolean) {
        viewModelScope.launch {
            settingsService.setIncludeVat(value)
        }
    }

    val uiState: StateFlow<UserMenuUiState> = accountService.firebaseUserFlow
        .flatMapLatest { user ->
            Log.d(TAG, "User: $user")
            if (user == null) {
                flowOf(UserMenuUiState.NotLoggedIn)
            } else {
                accountService.laiksUserFlow(user.uid)
                    .map { laiksUser ->
                        if (laiksUser != null) {
                            Log.d(TAG, "User Id ${user.uid} logged in")
                            UserMenuUiState.LoggedIn(
                                isAdmin = laiksUser.isAdmin,
                                isPricesAllowed = laiksUser.npAllowed,
                                displayName = user.displayName ?: "",
                                photoUrl = user.photoUrl
                            )
                        } else {
                            UserMenuUiState.NotLoggedIn
                        }
                    }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            UserMenuUiState.NotLoggedIn,
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
        const val TAG = "UserMenuViewModel"
    }


}
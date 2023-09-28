package com.folkmanis.laiks.ui.screens.user_menu

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.AuthUI
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.PermissionsService
import com.folkmanis.laiks.model.LaiksUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserMenuViewModel @Inject constructor(
    private val accountService: AccountService,
    private val laiksUserService: LaiksUserService,
    private val permissionsService: PermissionsService,
) : ViewModel() {


    fun setVat(value: Boolean) {
        viewModelScope.launch {
            laiksUserService.setVatEnabled(value)
        }
    }

    private val _uiState = MutableStateFlow<UserMenuUiState>(UserMenuUiState.NotLoggedIn)
    val uiState: StateFlow<UserMenuUiState> = _uiState.asStateFlow()

    suspend fun setUser(laiksUser: LaiksUser?) {
        if (laiksUser == null) {
            _uiState.value = UserMenuUiState.NotLoggedIn
        } else {
            val user = accountService.authUser
            val permissions = permissionsService.getPermissions(laiksUser.id)
            _uiState.value = UserMenuUiState.LoggedIn(
                isAdmin = permissions.admin,
                isPricesAllowed = permissions.npUser,
                displayName = laiksUser.name,
                photoUrl = user?.photoUrl,
                includeVat = laiksUser.includeVat,
                userId = laiksUser.id,
            )
        }
    }


    fun login() {
        viewModelScope.launch {
            val user = accountService.authUser
            if (user != null && !laiksUserService.userExists(user.uid)) {
                laiksUserService.createLaiksUser(user)
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
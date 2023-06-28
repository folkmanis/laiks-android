package com.folkmanis.laiks.ui.screens.user_menu

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.AuthUI
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.PermissionsService
import com.folkmanis.laiks.data.domain.IsPermissionUseCase
import com.folkmanis.laiks.data.domain.LaiksUserUseCase
import com.folkmanis.laiks.data.domain.NpUpdateUseCase
import com.folkmanis.laiks.model.LaiksUser
import com.folkmanis.laiks.ui.snackbar.SnackbarManager
import com.folkmanis.laiks.ui.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class UserMenuViewModel @Inject constructor(
    private val accountService: AccountService,
    private val npUpdate: NpUpdateUseCase,
    private val snackbarManager: SnackbarManager,
    private val laiksUserService: LaiksUserService,
    private val permissionsService: PermissionsService,
) : ViewModel() {


    fun setVat(userId: String, value: Boolean) {
        viewModelScope.launch {
            laiksUserService.setVatEnabled(userId, value)
        }
    }

    private val _uiState = MutableStateFlow<UserMenuUiState>(UserMenuUiState.NotLoggedIn)
    val uiState: StateFlow<UserMenuUiState> = _uiState.asStateFlow()

    suspend fun setUser(laiksUser: LaiksUser?) {
        if (laiksUser == null) {
            _uiState.value = UserMenuUiState.NotLoggedIn
        } else {
            val permissions = permissionsService.getPermissions(laiksUser.id)
            val user = accountService.authUser
            _uiState.value = UserMenuUiState.LoggedIn(
                isAdmin = permissions.admin,
                isPricesAllowed = permissions.npUser,
                isNpUploadAllowed = laiksUser.npUploadAllowed,
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

    fun updateNpPrices() {
        Log.d(TAG, "NpUpdate requested")
        viewModelScope.launch {
            try {
                val newRecords = npUpdate()
                Log.d(TAG, "$newRecords retrieved")
                snackbarManager.showMessage(
                    R.plurals.prices_retrieved,
                    newRecords,
                    newRecords
                )
            } catch (err: Throwable) {
                snackbarManager.showMessage(
                    err.toSnackbarMessage()
                )
                Log.e(TAG, "Error: $err")
            }
        }
    }


    companion object {
        const val TAG = "UserMenuViewModel"
    }


}
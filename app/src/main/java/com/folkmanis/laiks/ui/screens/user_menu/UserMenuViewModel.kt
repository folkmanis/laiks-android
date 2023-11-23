package com.folkmanis.laiks.ui.screens.user_menu

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.PermissionsService
import com.folkmanis.laiks.model.LaiksUser
import com.folkmanis.laiks.ui.snackbar.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserMenuViewModel @Inject constructor(
    private val accountService: AccountService,
    private val laiksUserService: LaiksUserService,
    private val permissionsService: PermissionsService,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {


    fun setVat(value: Boolean) {
        viewModelScope.launch {
            laiksUserService.setVatEnabled(value)
        }
    }

    private val userFlow = MutableStateFlow<LaiksUser?>(null)

    fun setUser(laiksUser: LaiksUser?) {
        userFlow.value = laiksUser
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: Flow<UserMenuUiState> = userFlow.flatMapLatest { user ->
        user?.let { laiksUser ->
            combine(
                permissionsService.npBlockedFlow(laiksUser.id),
                permissionsService.isAdminFlow(laiksUser.id)
            ) { npBlocked, isAdmin ->
                UserMenuUiState.LoggedIn(
                    isAdmin = isAdmin,
                    isPricesAllowed = !npBlocked,
                    photoUrl = accountService.authUser?.photoUrl,
                    displayName = laiksUser.name,
                    includeVat = laiksUser.includeVat,
                    userId = laiksUser.id,
                )
            }
        } as Flow<UserMenuUiState>?
            ?: flowOf(UserMenuUiState.NotLoggedIn) as Flow<UserMenuUiState>
    }

    fun onLoginResult(result: FirebaseAuthUIAuthenticationResult, afterLogin: () -> Unit) {
        if (result.resultCode == Activity.RESULT_OK) {
            loginSuccess(afterLogin)
        }
    }

    private fun loginSuccess(afterLogin: () -> Unit) {
        viewModelScope.launch {
            val user = accountService.authUser
            if (user != null && !laiksUserService.userExists(user.uid)) {
                laiksUserService.createLaiksUser(user)
            }
            snackbarManager.showMessage(R.string.login_success)
            afterLogin()
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
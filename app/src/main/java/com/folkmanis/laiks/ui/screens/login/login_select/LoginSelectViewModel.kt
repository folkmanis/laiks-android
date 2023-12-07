package com.folkmanis.laiks.ui.screens.login.login_select

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.ui.snackbar.SnackbarManager
import com.folkmanis.laiks.ui.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginSelectViewModel @Inject constructor(
    private val snackbarManager: SnackbarManager,
    private val accountService: AccountService,
    private val laiksUserService: LaiksUserService,
) : ViewModel() {

    fun logout(context: Context) {
        Identity.getSignInClient(context)
            .signOut()
        accountService
            .signOut()
    }

    fun loginWithGoogle(tokenId: String, afterLogin: () -> Unit) {
        val credential = GoogleAuthProvider.getCredential(tokenId, null)
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                snackbarManager.showMessage(throwable.toSnackbarMessage())
            }
        ) {
            accountService.loginWithCredential(credential).user?.also {
                afterLogin()
            }
        }
    }

    fun registerWithGoogle(tokenId: String, onLaiksUserCreated: () -> Unit) {
        val credential = GoogleAuthProvider.getCredential(tokenId, null)
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                snackbarManager.showMessage(throwable.toSnackbarMessage())
            }
        ) {
            val result = accountService.linkWithCredential(credential)
            result.user?.also { user ->
                snackbarManager.showMessage(R.string.login_user_created)
                laiksUserService.createLaiksUser(user)
                onLaiksUserCreated()
            }
        }
    }


}
package com.folkmanis.laiks.ui.screens.login.login_select

import android.content.Context
import android.net.Uri
import android.util.Log
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

    fun registerWithGoogle(
        tokenId: String,
        displayName: String?,
        profilePictureUri: Uri?,
        onLaiksUserCreated: () -> Unit
    ) {

        val credential = GoogleAuthProvider.getCredential(tokenId, null)

        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                snackbarManager.showMessage(throwable.toSnackbarMessage())
            }
        ) {
            accountService.linkWithCredential(credential, displayName, profilePictureUri)
            accountService.authUser?.also { user ->
                Log.d(TAG, "Linked with user ${user.email}, ${user.displayName}")
                val update = hashMapOf<String, Any?>(
                    "email" to user.email!!,
                    "name" to user.displayName!!,
                )
                Log.d(TAG, "Update: $update")
                laiksUserService.updateLaiksUser(update)
                snackbarManager.showMessage(R.string.login_user_created)
                onLaiksUserCreated()
            }
        }
    }

    companion object {
        private const val TAG = "LoginSelectViewModel"
    }

}
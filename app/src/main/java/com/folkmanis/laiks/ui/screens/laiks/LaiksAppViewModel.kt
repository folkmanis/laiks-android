package com.folkmanis.laiks.ui.screens.laiks

import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.ui.snackbar.SnackbarManager
import com.folkmanis.laiks.ui.snackbar.SnackbarMessage.Companion.toMessage
import com.folkmanis.laiks.ui.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.folkmanis.laiks.utilities.AnonymousAuthenticationFailedException
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaiksAppViewModel @Inject constructor(
    private val snackbarManager: SnackbarManager,
    private val laiksUserService: LaiksUserService,
    private val accountService: AccountService,
) : ViewModel() {

    private val _appState = MutableStateFlow(LaiksAppState())
    val appState = _appState.asStateFlow()

    val snackbarHostState = SnackbarHostState()

    fun setTitle(title: String) {
        _appState.update { state -> state.copy(title = title) }
    }

    fun initialize(
        resources: Resources,
    ) {
        viewModelScope.launch {
            snackbarManager
                .snackbarMessages
                .filterNotNull()
                .collect { snackbarMessage ->
                    val text = snackbarMessage.toMessage(resources)
                    snackbarHostState.showSnackbar(text)
                }
        }

        viewModelScope.launch {
            laiksUserService.laiksUserFlow().collect { user ->
                _appState.update { state ->
                    state.copy(laiksUser = user)
                }
            }
        }

        viewModelScope.launch {
            accountService.firebaseUserFlow.collect { user ->
                Log.d(TAG, "User ${user?.uid}")
                if (user == null) createAnonymousUser()
                _appState.update { state ->
                    state.copy(user = user)
                }
            }
        }

        viewModelScope.launch {
            laiksUserService.npAllowedFlow.collect { npAllowed ->
                _appState.update { state ->
                    state.copy(npBlocked = !npAllowed)
                }
            }
        }


    }

    fun logOut(context: Context) {
        Identity.getSignInClient(context)
            .signOut()
        accountService
            .signOut()
    }

    fun setVatEnabled(value: Boolean) {
        viewModelScope.launch {
            _appState.update { state ->
                val laiksUser = state.laiksUser?.copy(includeVat = value)
                state.copy(laiksUser = laiksUser)
            }
            _appState.value.laiksUser?.also {
                    laiksUserService.updateLaiksUser("includeVat", value)
            }
        }
    }

    private suspend fun createAnonymousUser() {
        try {
            val result = accountService.createAnonymous()
            result.user?.also { user ->
                laiksUserService.createLaiksUser(user)
                Log.d(TAG, "Anonymous user ${user.uid} created")
            } ?: throw AnonymousAuthenticationFailedException()
        } catch (err: AnonymousAuthenticationFailedException) {
            Log.e(TAG, "${err.message}")
            snackbarManager.showMessage(err.toSnackbarMessage())
        }
    }

    companion object {
        private const val TAG = "LaiksAppViewModel"
    }

}
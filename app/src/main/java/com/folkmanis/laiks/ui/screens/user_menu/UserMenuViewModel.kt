package com.folkmanis.laiks.ui.screens.user_menu

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.PermissionsService
import com.folkmanis.laiks.model.LaiksUser
import com.google.android.gms.auth.api.identity.Identity
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

    fun logout(context: Context) {
        Identity.getSignInClient(context)
            .signOut()
        accountService
            .signOut()
    }

    companion object {
        const val TAG = "UserMenuViewModel"
    }


}
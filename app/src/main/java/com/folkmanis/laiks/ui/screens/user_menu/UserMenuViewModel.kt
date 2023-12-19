package com.folkmanis.laiks.ui.screens.user_menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.domain.NpBlockedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserMenuViewModel @Inject constructor(
    accountService: AccountService,
    npBlocked: NpBlockedUseCase,
    private val laiksUserService: LaiksUserService,
) : ViewModel() {

    private val expandedFlow = MutableStateFlow(false)

    fun close() {
        expandedFlow.value = false
    }

    fun toggle() {
        expandedFlow.value = !expandedFlow.value
    }

    fun setVatEnabled(value: Boolean) {
        viewModelScope.launch {
            laiksUserService.updateLaiksUser("includeVat", value)
        }
    }


    val uiState = combine(
        laiksUserService.laiksUserFlow(),
        accountService.firebaseUserFlow,
        expandedFlow,
        npBlocked()
    ) { laiksUser, user, expanded, isNpBlocked ->
        UserMenuState(
            expanded = expanded,
            isVatEnabled = laiksUser?.includeVat ?: false,
            isAnonymous = user?.isAnonymous ?: false,
            npBlocked = isNpBlocked,
            photoUrl = user?.photoUrl,
            name = laiksUser?.name,
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = UserMenuState()
        )

}
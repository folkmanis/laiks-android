package com.folkmanis.laiks.ui.screens.users

import androidx.lifecycle.ViewModel
import com.folkmanis.laiks.data.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    accountService: AccountService,
    ) : ViewModel() {

val uiState: Flow<UsersUiState> = accountService.laiksUsersFlow()
    .map { users ->
        UsersUiState.Success(users)
    }

}
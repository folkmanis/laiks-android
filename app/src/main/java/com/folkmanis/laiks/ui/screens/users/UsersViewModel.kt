package com.folkmanis.laiks.ui.screens.users

import androidx.lifecycle.ViewModel
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.domain.LaiksUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    laiksUserService: LaiksUserService
    ) : ViewModel() {

val uiState: Flow<UsersUiState> = laiksUserService.laiksUsersFlow()
    .map { users ->
        UsersUiState.Success(users)
    }

}
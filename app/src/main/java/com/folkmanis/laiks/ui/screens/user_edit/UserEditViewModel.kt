package com.folkmanis.laiks.ui.screens.user_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.model.LaiksUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LaiksUserState(
    val laiksUser: LaiksUser = LaiksUser(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isCurrentUser: Boolean = false,
) {
    val enabled: Boolean
        get() = !isLoading && laiksUser.id.isNotEmpty() && !isSaving
}

@HiltViewModel
class UserEditViewModel @Inject constructor(
    private val accountService: AccountService,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LaiksUserState())
    val uiState = _uiState.asStateFlow()

    fun setNpAllowed(value: Boolean) {
        _uiState.update {
            val user = with(it.laiksUser) {
                copy(npAllowed = value)
            }
            it.copy(laiksUser = user)
        }
        saveUser()
    }

    fun setNpUploadAllowed(value: Boolean) {
        _uiState.update {
            val user= with(it.laiksUser) {
                copy(npUploadAllowed = value)
            }
            it.copy(laiksUser = user)
        }
        saveUser()
    }

    fun setIsAdmin(value: Boolean) {
        _uiState.update {
            val user = with(it.laiksUser) {
                copy(isAdmin = value)
            }
            it.copy(laiksUser = user)
        }
        saveUser()
    }

    private fun saveUser() {
        _uiState.update { it.copy(isSaving = true) }

        viewModelScope.launch {
            _uiState.update { state ->
                accountService.updateLaiksUser(state.laiksUser)
                state.copy(isSaving = false)
            }
        }
    }

    fun loadUser(id: String) {

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            accountService.laiksUser(id).let { user ->
                if (user == null) {
                    _uiState.update { it.copy(isLoading = false) }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            laiksUser = user,
                            isCurrentUser = user.id == accountService.authUser?.uid
                        )
                    }
                }
            }

        }
    }

}
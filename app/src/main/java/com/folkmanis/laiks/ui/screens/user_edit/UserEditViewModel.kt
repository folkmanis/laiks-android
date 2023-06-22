package com.folkmanis.laiks.ui.screens.user_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.PermissionsService
import com.folkmanis.laiks.model.LaiksUser
import com.folkmanis.laiks.model.Permissions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LaiksUserState(
    val laiksUser: LaiksUser = LaiksUser(),
    val permissions: Permissions = Permissions(),
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
    private val permissionsService: PermissionsService,
    private val laiksUserService: LaiksUserService,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LaiksUserState())
    val uiState = _uiState.asStateFlow()

    fun setNpAllowed(value: Boolean) {
        _uiState.update {
            val permissions = with(it.permissions) {
                copy(npUser = value)
            }
            updatePermission("npUser", value)
            it.copy(permissions = permissions)
        }
    }

    fun setNpUploadAllowed(value: Boolean) {
        _uiState.update {
            val user = with(it.laiksUser) {
                copy(npUploadAllowed = value)
            }
            it.copy(laiksUser = user)
        }
        saveUser()
    }

    fun setIsAdmin(value: Boolean) {
        _uiState.update {
            val permissions = with(it.permissions) {
                copy(admin = value)
            }
            updatePermission("admin", value)
            it.copy(permissions = permissions)
        }
    }

    private fun updatePermission(field: String, value: Boolean) {
        _uiState.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            _uiState.update { state ->
                permissionsService.updatePermission(state.laiksUser.id, field, value)
                state.copy(isSaving = false)
            }
        }
    }

    private fun saveUser() {
        _uiState.update { it.copy(isSaving = true) }

        viewModelScope.launch {
            _uiState.update { state ->
                laiksUserService.updateLaiksUser(state.laiksUser)
                state.copy(isSaving = false)
            }
        }
    }

    fun loadUser(id: String) {

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            laiksUserService.laiksUser(id).let { user ->
                if (user != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            laiksUser = user,
                            isCurrentUser = user.id == accountService.authUser?.uid
                        )
                    }
                }
                user?.id
            }.let { id ->
                if (id != null) {
                    permissionsService.getPermissions(id).let { permissions ->
                        _uiState.update { it.copy(permissions = permissions) }
                    }
                }
                _uiState.update { it.copy(isLoading = false) }
            }

        }
    }

}
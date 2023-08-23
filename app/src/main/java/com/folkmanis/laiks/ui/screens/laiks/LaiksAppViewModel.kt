package com.folkmanis.laiks.ui.screens.laiks

import android.content.res.Resources
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.ui.snackbar.SnackbarManager
import com.folkmanis.laiks.ui.snackbar.SnackbarMessage.Companion.toMessage
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
                    state.copy(user = user)
                }
            }
        }

    }

}
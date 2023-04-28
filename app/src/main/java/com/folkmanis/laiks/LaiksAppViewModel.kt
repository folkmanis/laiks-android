package com.folkmanis.laiks

import android.content.res.Resources
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.model.LaiksUser
import com.folkmanis.laiks.utilities.snackbar.SnackbarManager
import com.folkmanis.laiks.utilities.snackbar.SnackbarMessage.Companion.toMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaiksAppViewModel @Inject constructor(
    private val accountService: AccountService,
) : ViewModel() {

    private val _appState = MutableStateFlow<LaiksAppState>(LaiksAppState.Loading)
    val appState = _appState.asStateFlow()

    private val snackbarManager = SnackbarManager
    private val snackbarHostState = SnackbarHostState()


    fun setState(
        windowSize: WindowSizeClass,
        navController: NavHostController,
        resources: Resources,
    ) {

        _appState.value = LaiksAppState.Loaded(
            windowSize = windowSize,
            navController = navController,
            user = null,
            snackbarHostState = snackbarHostState,
            resources = resources,
        )

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
            accountService.laiksUserFlow.collect { user ->
                _appState.update { state ->
                    if (state is LaiksAppState.Loaded) {
                        state.copy(user = user)
                    } else state
                }
            }
        }


    }

}
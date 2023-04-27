package com.folkmanis.laiks

import android.content.res.Resources
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.model.LaiksUser
import com.folkmanis.laiks.utilities.snackbar.SnackbarManager
import com.folkmanis.laiks.utilities.snackbar.SnackbarMessage.Companion.toMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaiksAppViewModel @Inject constructor(
    accountService: AccountService,
) : ViewModel() {

    val laiksUser = accountService.laiksUserFlow

    private val snackbarManager = SnackbarManager

    init {
        viewModelScope.launch {
            snackbarManager
                .snackbarMessages
                .filterNotNull()
                .collect { snackbarMessage ->
                    val text = snackbarMessage.toMessage(resources)
                    snackbarHostState.showSnackbar(text)
                }
        }

    }

    fun setState(
        windowSize: WindowSizeClass,
        navController: NavHostController,
        snackbarHostState: SnackbarHostState,
        snackbarManager: SnackbarManager,
        resources: Resources,

        ) {

    }

}
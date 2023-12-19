package com.folkmanis.laiks.ui.screens.user_menu

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun UserMenu(
    onUserSettings: (String?) -> Unit,
    onEditAppliances: () -> Unit,
    onLogin: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UserMenuViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()

    fun close(action: () -> Unit = {}) {
        viewModel.close()
        action()
    }

    Box(modifier = modifier) {
        UserMenuIconButton(
            photoUrl = uiState.photoUrl,
            onClick = viewModel::toggle
        )

        DropdownMenu(
            expanded = uiState.expanded,
            onDismissRequest = { close() }
        ) {

            if (uiState.isAnonymous)
                LoginMenuItem(onClick = { close(onLogin) })
            else
                LogoutMenuItem(onClick = { close(onLogout) })

            SettingsMenuItem(onClick = {
                close {
                    onUserSettings(uiState.name)
                }
            })

            if (!uiState.npBlocked) {

                AppliancesMenuItem(onClick = { close(onEditAppliances) })

                VatEnabledMenuItem(
                    isVatEnabled = uiState.isVatEnabled,
                    onClick = {
                        close { viewModel.setVatEnabled(it) }
                    }
                )
            }

        }
    }

}
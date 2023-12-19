package com.folkmanis.laiks.ui.screens.user_menu

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.folkmanis.laiks.model.LaiksUser
import com.google.firebase.auth.FirebaseUser


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
        .collectAsStateWithLifecycle(UserMenuState())

    val close = viewModel::close

    Box(modifier = modifier) {
        UserMenuIconButton(
            photoUrl = uiState.photoUrl,
            onClick = viewModel::toggle
        )

        DropdownMenu(
            expanded = uiState.expanded,
            onDismissRequest = close
        ) {

            if (uiState.isAnonymous)
                LoginMenuItem(onClick = {
                    close()
                    onLogin()
                })
            else
                LogoutMenuItem(onClick = {
                    close()
                    onLogout()
                })

            SettingsMenuItem(onClick = {
                close()
                onUserSettings(uiState.name)
            })

            if (!uiState.npBlocked) {

                AppliancesMenuItem(onClick = {
                    close()
                    onEditAppliances()
                })

                VatEnabledMenuItem(
                    isVatEnabled = uiState.isVatEnabled,
                    onClick = {
                        close()
                        viewModel.setVatEnabled(it)
                    })
            }

        }
    }

}
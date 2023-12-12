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
    onUserSettings: () -> Unit,
    onEditAppliances: () -> Unit,
    onLogin: () -> Unit,
    onLogout: () -> Unit,
    onSetVatEnabled: (Boolean) -> Unit,
    isVatEnabled: Boolean,
    isAnonymous: Boolean,
    npBlocked: Boolean,
    photoUrl: Uri?,
    modifier: Modifier = Modifier,
) {

    var menuExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        UserMenuIconButton(
            photoUrl = photoUrl,
            onClick = { menuExpanded = !menuExpanded }
        )

        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false }
        ) {

            if (isAnonymous)
                LoginMenuItem(onClick = {
                    menuExpanded = false
                    onLogin()
                })
            else
                LogoutMenuItem(onClick = {
                    menuExpanded = false
                    onLogout()
                })

            SettingsMenuItem(onClick = {
                menuExpanded = false
                onUserSettings()
            })

            if (!npBlocked) {

                AppliancesMenuItem(onClick = {
                    menuExpanded = false
                    onEditAppliances()
                })

                VatEnabledMenuItem(
                    isVatEnabled = isVatEnabled,
                    onClick = {
                        menuExpanded = false
                        onSetVatEnabled(it)
                    })
            }

        }
    }

}
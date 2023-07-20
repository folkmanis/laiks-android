package com.folkmanis.laiks.ui.screens.user_menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.utilities.composables.AvatarIcon

@Composable
fun LoggedInUserMenu(
    state: UserMenuUiState.LoggedIn,
    onLogout: () -> Unit,
    isVat: Boolean,
    onSetVat: (Boolean) -> Unit,
    onNpPricesReload: () -> Unit,
    onUserSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {

    var menuExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        IconButton(onClick = { menuExpanded = !menuExpanded }) {
            if (state.photoUrl != null) {
                AvatarIcon(
                    photoUrl = state.photoUrl,
                    displayName = state.displayName,
                    modifier = Modifier.size(30.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = stringResource(id = R.string.login_button),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
        ) {
            // Logout
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = R.string.logout_menu_item))
                },
                onClick = {
                    menuExpanded = false
                    onLogout()
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.logout),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            )
            DropdownMenuItem(
                text = {
                    Text(stringResource(R.string.settings))
                },
                onClick = {
                    menuExpanded = false
                    onUserSettings()
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                }
            )
            // PVN
            if (state.isPricesAllowed) {
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = R.string.include_vat))
                    },
                    onClick = {
                        onSetVat(!isVat)
                        menuExpanded = false
                    },
                    leadingIcon = {
                        if (isVat) {
                            Icon(Icons.Filled.Check, contentDescription = null)
                        }
                    }
                )
            }
            // Reload
            if (state.isNpUploadAllowed) {
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = R.string.load_np_data))
                    },
                    onClick = {
                        menuExpanded = false
                        onNpPricesReload()
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                        )

                    }
                )
            }
        }

    }
}


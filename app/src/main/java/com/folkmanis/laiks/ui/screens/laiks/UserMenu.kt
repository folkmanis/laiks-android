package com.folkmanis.laiks.ui.screens.laiks

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.utilities.composables.AvatarIcon

@Composable
fun LoggedInUserMenu(
    photoUrl: Uri?,
    displayName: String,
    isAdmin: Boolean,
    npAllowed: Boolean,
    onLogout: () -> Unit,
    isVat: Boolean,
    onSetVat: (Boolean) -> Unit,
    onUsersAdmin: () -> Unit,
    onAppliancesAdmin: () -> Unit,
    modifier: Modifier = Modifier,
) {

    var menuExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        IconButton(onClick = { menuExpanded = !menuExpanded }) {
            if (photoUrl != null) {
                AvatarIcon(
                    photoUrl = photoUrl,
                    displayName = displayName,
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
            // PVN
            if (npAllowed) {
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
            // Admin
            if (isAdmin) {
                // Users
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = R.string.users_menu))
                    },
                    onClick = {
                        menuExpanded = false
                        onUsersAdmin()
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.manage_accounts),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                )
                // Appliances
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = R.string.appliances_menu_item))
                    },
                    onClick = {
                        menuExpanded = false
                        onAppliancesAdmin()
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.dishwasher_gen),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                )
            }
        }

    }
}

@Composable
fun NotLoggedUserMenu(
    onLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {

    var menuExpanded by remember { mutableStateOf(false) }


    Box(modifier = modifier) {
        IconButton(onClick = { menuExpanded = !menuExpanded }) {
            Icon(
                imageVector = Icons.Outlined.AccountCircle,
                contentDescription = stringResource(id = R.string.login_button),
                modifier = Modifier.size(24.dp)
            )
        }
        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = R.string.login_button))
                },
                onClick = {
                    menuExpanded = false
                    onLogin()
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.login),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                }
            )
        }
    }

}

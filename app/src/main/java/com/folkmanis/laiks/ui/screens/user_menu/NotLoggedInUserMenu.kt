package com.folkmanis.laiks.ui.screens.user_menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R

@Composable
fun NotLoggedInUserMenu(
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

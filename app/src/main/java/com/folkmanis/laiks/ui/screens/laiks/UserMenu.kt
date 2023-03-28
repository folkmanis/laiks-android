package com.folkmanis.laiks.ui.screens.laiks

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.folkmanis.laiks.R

@Composable
fun LoggedInUserMenu(
    photoUrl: Uri?,
    displayName: String,
    onLogout: () -> Unit,
    isVat: Boolean,
    onSetVat: (Boolean) -> Unit,
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
                    displayName = displayName
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

@Composable
fun AvatarIcon(
    photoUrl: Uri,
    displayName: String,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(photoUrl)
            .build(),
        contentDescription = displayName,
        contentScale = ContentScale.FillBounds,
        modifier = modifier
            .size(30.dp)
            .clip(shape = CircleShape)
    )
}

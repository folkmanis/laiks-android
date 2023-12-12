package com.folkmanis.laiks.ui.screens.user_menu

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R

@Composable
fun LogoutMenuItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenuItem(
        text = {
            Text(text = stringResource(R.string.logout_menu_item))
        },
        onClick = onClick,
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.logout),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        },
        modifier = modifier
    )

}

@Composable
fun LoginMenuItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenuItem(
        text = {
            Text(text = stringResource(id = R.string.login_button))
        },
        onClick = onClick,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.login),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        },
        modifier = modifier,
    )

}

@Composable
fun SettingsMenuItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenuItem(
        text = {
            Text(stringResource(R.string.settings))
        },
        onClick = onClick,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        },
        modifier = modifier,
    )

}

@Composable
fun AppliancesMenuItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenuItem(
        text = {
            Text(text = stringResource(R.string.appliances_menu_item))
        },
        onClick = onClick,
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.dishwasher_gen),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        },
        modifier = modifier,
    )

}

@Composable
fun VatEnabledMenuItem(
    modifier: Modifier=Modifier,
    isVatEnabled: Boolean,
    onClick: (Boolean) -> Unit,
) {
    DropdownMenuItem(
        text = {
            Text(text = stringResource(R.string.include_vat))
        },
        onClick = {
            onClick(!isVatEnabled)
        },
        leadingIcon = {
            if (isVatEnabled) {
                Icon(Icons.Filled.Check, contentDescription = null)
            }
        },
        modifier = modifier,
    )

}
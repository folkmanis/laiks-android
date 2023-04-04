package com.folkmanis.laiks.ui.screens.user_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.folkmanis.laiks.R
import com.folkmanis.laiks.model.LaiksUser

@Composable
fun UserEditScreen(
    modifier: Modifier = Modifier,
    viewModel: UserEditViewModel = hiltViewModel(),
) {

    val userState by viewModel.uiState.collectAsStateWithLifecycle()

    UserFormScreen(
        laiksUser = userState.laiksUser,
        enabled = userState.enabled,
        onIsAdminChange = viewModel::setIsAdmin,
        adminChangeEnabled = !userState.isCurrentUser,
        onNpAllowedChange = viewModel::setNpAllowed,
        modifier = modifier,
    )

}

@Composable
fun UserFormScreen(
    laiksUser: LaiksUser,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    adminChangeEnabled: Boolean = false,
    onNpAllowedChange: () -> Unit = {},
    onIsAdminChange: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        Text(
            text = laiksUser.name,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = laiksUser.email,
            style = MaterialTheme.typography.titleSmall
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = laiksUser.npAllowed,
                onCheckedChange = { onNpAllowedChange() },
                enabled = enabled,
            )
            Text(text = stringResource(id = R.string.np_allowed_checkbox))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = laiksUser.isAdmin,
                onCheckedChange = { onIsAdminChange() },
                enabled = enabled && adminChangeEnabled
            )
            Text(text = stringResource(id = R.string.is_admin_checkbox))
        }
    }

}

@Preview(locale = "lv")
@Composable
fun UserFormScreenPreview() {
    val user = LaiksUser(
        id = "123",
        email = "user@example.com",
        isAdmin = false,
        npAllowed = true,
        name = "Example User"
    )
    MaterialTheme {
        UserFormScreen(laiksUser = user)
    }
}
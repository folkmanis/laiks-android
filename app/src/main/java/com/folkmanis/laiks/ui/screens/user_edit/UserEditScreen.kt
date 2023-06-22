package com.folkmanis.laiks.ui.screens.user_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.folkmanis.laiks.R
import com.folkmanis.laiks.model.LaiksUser
import com.folkmanis.laiks.model.Permissions

@Composable
internal fun UserEditScreen(
    viewModel: UserEditViewModel,
    setTitle: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    val userState by viewModel.uiState.collectAsStateWithLifecycle()

    val title = userState.laiksUser.name
    LaunchedEffect(title) {
        setTitle(title)
    }

    UserFormScreen(
        laiksUser = userState.laiksUser,
        permissions = userState.permissions,
        modifier = modifier,
        enabled = userState.enabled,
        onIsAdminChange = viewModel::setIsAdmin,
        adminChangeEnabled = !userState.isCurrentUser,
        onNpAllowedChange = viewModel::setNpAllowed,
        onNpUploadAllowedChange = viewModel::setNpUploadAllowed,
    )

}


@Composable
fun UserFormScreen(
    laiksUser: LaiksUser,
    permissions: Permissions,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    adminChangeEnabled: Boolean = false,
    onNpAllowedChange: (Boolean) -> Unit = {},
    onIsAdminChange: (Boolean) -> Unit = {},
    onNpUploadAllowedChange: (Boolean) -> Unit = {},
) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        Text(
            text = laiksUser.email,
            style = MaterialTheme.typography.titleSmall
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = permissions.npUser,
                onCheckedChange = onNpAllowedChange,
                enabled = enabled,
            )
            Text(text = stringResource(id = R.string.np_allowed_checkbox))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = laiksUser.npUploadAllowed,
                onCheckedChange = onNpUploadAllowedChange,
                enabled = enabled,
            )
            Text(text = stringResource(id = R.string.np_upload_allowed_checkbox))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = permissions.admin,
                onCheckedChange = onIsAdminChange,
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
        name = "Example User",
    )
    val permissions = Permissions(npUser = true)
    MaterialTheme {
        UserFormScreen(laiksUser = user, permissions = permissions)
    }
}
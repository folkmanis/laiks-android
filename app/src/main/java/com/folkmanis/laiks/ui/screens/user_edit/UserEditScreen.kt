@file:OptIn(ExperimentalMaterial3Api::class)

package com.folkmanis.laiks.ui.screens.user_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.folkmanis.laiks.R
import com.folkmanis.laiks.model.LaiksUser

@Composable
internal fun UserEditScreen(
    onNavigateBack: () -> Unit,
    viewModel: UserEditViewModel,
    modifier: Modifier = Modifier,
) {

    val userState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        userState.laiksUser.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                },
            )

        },
        modifier = modifier,
    ) { innerPadding ->

        UserFormScreen(
            laiksUser = userState.laiksUser,
            modifier = Modifier.padding(innerPadding),
            enabled = userState.enabled,
            onIsAdminChange = viewModel::setIsAdmin,
            adminChangeEnabled = !userState.isCurrentUser,
            onNpAllowedChange = viewModel::setNpAllowed,
            onNpUploadAllowedChange = viewModel::setNpUploadAllowed,
        )

    }

}

@Composable
fun UserFormScreen(
    laiksUser: LaiksUser,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    adminChangeEnabled: Boolean = false,
    onNpAllowedChange: (Boolean) -> Unit = {},
    onIsAdminChange: (Boolean) -> Unit = {},
    onNpUploadAllowedChange: (Boolean)->Unit={},
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
                checked = laiksUser.npAllowed,
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
                checked = laiksUser.isAdmin,
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
        isAdmin = false,
        npAllowed = true,
        name = "Example User"
    )
    MaterialTheme {
        UserFormScreen(laiksUser = user)
    }
}
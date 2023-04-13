package com.folkmanis.laiks.ui.screens.users

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.folkmanis.laiks.R
import com.folkmanis.laiks.model.LaiksUser
import com.folkmanis.laiks.utilities.composables.ErrorScreen
import com.folkmanis.laiks.utilities.composables.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UsersScreen(
    onEdit: (String) -> Unit,
    state: UsersUiState,
    popUp: () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.users_screen),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = popUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                },
                actions = actions,
            )
        },
        modifier = modifier,
    ) { innerPadding ->

        when (state) {
            is UsersUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    items(state.users, key = { it.id }) { user ->
                        UserRow(
                            user = user,
                            onEdit = { onEdit(user.id) },
                        )
                        Divider(thickness = 2.dp)
                    }
                }
            }

            is UsersUiState.Error -> ErrorScreen(
                reason = state.reason,
                modifier = Modifier.padding(innerPadding)
            )
            is UsersUiState.Loading -> LoadingScreen(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
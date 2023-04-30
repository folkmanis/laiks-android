package com.folkmanis.laiks.ui.screens.users

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.folkmanis.laiks.utilities.composables.ErrorScreen
import com.folkmanis.laiks.utilities.composables.LoadingScreen

@Composable
internal fun UsersScreen(
    onEdit: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UsersViewModel = hiltViewModel(),
) {

    val state = viewModel.uiState
        .collectAsStateWithLifecycle(initialValue = UsersUiState.Loading)
        .value


    Box(modifier = modifier.fillMaxSize()) {

        when (state) {
            is UsersUiState.Success -> {
                LazyColumn {
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
            )

            is UsersUiState.Loading -> LoadingScreen()
        }

    }

}

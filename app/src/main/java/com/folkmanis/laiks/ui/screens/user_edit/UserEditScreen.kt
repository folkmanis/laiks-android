package com.folkmanis.laiks.ui.screens.user_edit

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun UserEditScreen(
    id: String?,
    modifier: Modifier = Modifier,
) {

    if (id != null) {
        Text(
            text = id,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
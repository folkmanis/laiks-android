package com.folkmanis.laiks.utilities.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R

@Composable
fun ButtonRow(
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
    modifier: Modifier = Modifier,
    saveEnabled: Boolean = true,
) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                end = 16.dp,
                start = 16.dp,
                bottom = 8.dp
            )
    ) {
        TextButton(onClick = onDismiss) {
            Text(text = stringResource(id = R.string.action_cancel))
        }
        TextButton(
            onClick = onAccept,
            enabled = saveEnabled,
        ) {
            Text(text = stringResource(id = R.string.action_save))
        }
    }
}

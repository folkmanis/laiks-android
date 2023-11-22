package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.folkmanis.laiks.R

@Composable
fun DeleteAccountButton(
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {

    var deleteConfirmation by remember {
        mutableStateOf(false)
    }

    OutlinedButton(
        onClick = { deleteConfirmation = true },
        modifier = modifier,
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = null,
            modifier = Modifier
                .size(ButtonDefaults.IconSize)
        )
        Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
        Text(text = stringResource(R.string.user_delete_account_button))
    }

    if (deleteConfirmation) {
        DeleteConfirmationDialog(
            onConfirm = {
                deleteConfirmation = false
                onDelete()
            },
            onCancel = { deleteConfirmation = false })
    }

}

@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {

    AlertDialog(
        onDismissRequest = onCancel,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(R.string.alert_ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(text = stringResource(R.string.alert_dismiss))
            }
        },
        icon = {
            Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
        },
        text = {
            Text(text = stringResource(id = R.string.user_delete_confirmation))
        },
        title = {
            Text(text = stringResource(id = R.string.user_delete_account_button))
        },
        modifier = modifier,
    )

}
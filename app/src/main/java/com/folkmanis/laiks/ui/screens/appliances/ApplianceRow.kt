package com.folkmanis.laiks.ui.screens.appliances

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.folkmanis.laiks.R
import com.folkmanis.laiks.model.PowerAppliance

@Composable
fun ApplianceRow(
    appliance: PowerAppliance,
    modifier: Modifier = Modifier,
    onEdit: (String) -> Unit = {},
    onDelete: (String) -> Unit = {},
) {

    var deleteConfirmation by remember {
        mutableStateOf(false)
    }

    val id = appliance.id
    val name = appliance.name

    ListItem(
        headlineContent = {
            Text(text = name)
        },
        modifier = modifier
            .clickable { onEdit(id) },
        trailingContent = {
            IconButton(onClick = { onDelete(id) }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(id = R.string.delete_button)
                )
            }
        }
    )

    if (deleteConfirmation) {
        DeleteConfirmation(
            onAccept = {
                deleteConfirmation = false
                onDelete(id)
            },
            onDismiss = { deleteConfirmation = false },
            name = name,
        )
    }

}

@Composable
fun DeleteConfirmation(
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
    name: String,
    modifier: Modifier = Modifier,
) {
AlertDialog(
    onDismissRequest = onDismiss,
    modifier = modifier,
    confirmButton = {
        TextButton(onClick = onAccept) {
            Text(text = stringResource(id = R.string.alert_ok))
        }
    },
    dismissButton = {
        TextButton(onClick = onDismiss) {
            Text(text = stringResource(id = R.string.alert_dismiss))
        }
    },
    icon = { Icon(imageVector = Icons.Filled.Delete, contentDescription = null)},
    text = {
        Text(text = stringResource(id = R.string.appliance_delete_confirmation, name))
    }
)
}

@Preview
@Composable
fun ApplianceRowPreview() {
    MaterialTheme {
        ApplianceRow(appliance = PowerAppliance(name = "Dishwasher"))
    }
}
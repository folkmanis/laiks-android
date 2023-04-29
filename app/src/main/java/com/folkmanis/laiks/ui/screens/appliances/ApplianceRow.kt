package com.folkmanis.laiks.ui.screens.appliances

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.folkmanis.laiks.R
import com.folkmanis.laiks.model.PowerAppliance

@Composable
fun ApplianceRow(
    appliance: PowerAppliance,
    isAdmin: Boolean,
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
        modifier = modifier,
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = Color(appliance.color.toColorInt()),
                        shape = CircleShape
                    )
            )
        },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isAdmin) {
                    IconButton(onClick = { onEdit(id) }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = stringResource(id = R.string.settings)
                        )
                    }
                    IconButton(onClick = {
                        deleteConfirmation = true
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(id = R.string.delete_button)
                        )
                    }
                }
            }
        }
    )

    if (deleteConfirmation && isAdmin) {
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
        icon = { Icon(imageVector = Icons.Filled.Delete, contentDescription = null) },
        text = {
            Text(text = stringResource(id = R.string.appliance_delete_confirmation, name))
        }
    )
}

@Preview
@Composable
fun ApplianceRowPreview() {
    MaterialTheme {
        ApplianceRow(
            appliance = PowerAppliance(name = "Dishwasher"),
            isAdmin = true
        )
    }
}
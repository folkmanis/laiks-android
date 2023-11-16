package com.folkmanis.laiks.ui.screens.user_settings.appliances

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.fake.FakeAppliancesService
import org.burnoutcrew.reorderable.ReorderableLazyListState
import org.burnoutcrew.reorderable.detectReorder
import org.burnoutcrew.reorderable.rememberReorderableLazyListState

@Composable
fun ApplianceRow(
    name: String,
    color: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onSchedule: () -> Unit,
    busy: Boolean,
    reorderableState: ReorderableLazyListState,
    modifier: Modifier = Modifier,
) {

    var deleteConfirmationActive by remember {
        mutableStateOf(false)
    }

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
                        color = Color(color.toColorInt()),
                        shape = CircleShape
                    )
            )
        },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {

                IconButton(
                    onClick = onSchedule,
                    enabled = !busy,
                ) {
                    Icon(
                        painterResource(R.drawable.schedule_24px),
                        contentDescription = stringResource(
                            R.string.appliance_costs_screen_open,
                            name
                        )
                    )
                }

                IconButton(
                    onClick = onEdit,
                    enabled = !busy,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(R.string.settings)
                    )
                }

                IconButton(
                    onClick = { deleteConfirmationActive = true },
                    enabled = !busy,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.delete_button)
                    )
                }

                Icon(
                    painter = painterResource(R.drawable.drag_indicator_24px),
                    contentDescription = stringResource(R.string.drag_handle),
                    modifier = Modifier
                        .size(24.dp)
                        .detectReorder(reorderableState),
                )
            }
        }
    )

    if (deleteConfirmationActive) {
        ApplianceDeleteConfirmation(
            name = name,
            onDismiss = {
                deleteConfirmationActive = false
            },
            onAccept = {
                deleteConfirmationActive = false
                onDelete()
            })
    }

}

@Composable
fun ApplianceDeleteConfirmation(
    name: String,
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onAccept) {
                Text(text = stringResource(id = R.string.delete_button))
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.action_cancel))
            }
        },
        icon = {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = null,
            )
        },
        title = {},
        text = {
            Text(
                text = stringResource(R.string.appliance_delete_confirmation, name),
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ApplianceDeleteConfirmationDialogPreview() {
    MaterialTheme {
        ApplianceDeleteConfirmation(
            name = "Dishwasher",
            onDismiss = { },
            onAccept = { })
    }
}

@Preview
@Composable
fun ApplianceRowPreview() {

    val reorderableState = rememberReorderableLazyListState(onMove = { _, _ -> })

    MaterialTheme {
        ApplianceRow(
            name = FakeAppliancesService.dishWasher.name,
            color = FakeAppliancesService.dishWasher.color,
            onDelete = {},
            onEdit = {},
            busy = true,
            reorderableState = reorderableState,
            onSchedule = {},
        )
    }
}


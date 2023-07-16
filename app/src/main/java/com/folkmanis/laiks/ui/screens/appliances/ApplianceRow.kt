package com.folkmanis.laiks.ui.screens.appliances

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.folkmanis.laiks.model.ApplianceType
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.model.PowerApplianceRecord

@Composable
fun ApplianceRow(
    appliance: PowerAppliance,
    type: Int,
    modifier: Modifier = Modifier,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onView: () -> Unit,
) {

    ListItem(
        headlineContent = {
            Text(text = appliance.name)
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
                when (type) {
                    ApplianceType.USER.type -> {
                        IconButton(onClick = onEdit) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = stringResource(id = R.string.settings)
                            )
                        }
                    }

                    ApplianceType.SYSTEM.type -> {
                        IconButton(onClick = onView) {
                            Icon(
                                painter = painterResource(id = R.drawable.expand_content),
                                contentDescription = stringResource(id = R.string.view_appliance),
                                modifier = Modifier.size(24.dp),
                            )
                        }
                    }
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(id = R.string.delete_button)
                    )
                }

                Icon(
                    painter = painterResource(R.drawable.drag_handle_48px),
                    contentDescription = stringResource(R.string.drag_handle),
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    )

}

@Preview
@Composable
fun UserApplianceRowPreview() {
    MaterialTheme {
        ApplianceRow(
            appliance = FakeAppliancesService.dishWasher,
            onDelete = {},
            onEdit = {},
            onView = {},
            type = ApplianceType.USER.type,
        )
    }
}

@Preview
@Composable
fun SystemApplianceRowPreview() {
    MaterialTheme {
        ApplianceRow(
            appliance = FakeAppliancesService.washer,
            onDelete = {},
            onEdit = {},
            onView = {},
            type = ApplianceType.SYSTEM.type,
        )
    }
}


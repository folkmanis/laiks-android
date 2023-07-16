package com.folkmanis.laiks.ui.screens.appliances

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.fake.FakeAppliancesService
import com.folkmanis.laiks.model.PowerApplianceRecord


@Composable
internal fun AppliancesScreen(
    records: List<PowerApplianceRecord>,
    modifier: Modifier = Modifier,
    onEdit: (idx: Int) -> Unit,
    onView: (idx: Int) -> Unit,
    onAdd: () -> Unit,
    onSelectAppliance: (idx:Int) -> Unit,
    onDelete: (idx: Int) -> Unit,
) {

    Box(modifier = modifier.fillMaxSize()) {

        LazyColumn {
            itemsIndexed(
                items = records,
                key = { _, item -> item.id + item.type.toString() }
            )
            { idx, record ->

                ApplianceRow(
                    appliance = record.appliance,
                    type = record.type,
                    onEdit = { onEdit(idx) },
                    onDelete = { onDelete(idx) },
                    onView = { onView(idx) },
                    modifier = Modifier
                        .clickable { onSelectAppliance(idx) }
                )

                Divider(thickness = 2.dp)

            }
        }

        FloatingActionButton(
            onClick = onAdd,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(id = R.string.add)
            )
        }

    }

}

@Preview
@Composable
fun AppliancesScreenPreview() {
    MaterialTheme {
        AppliancesScreen(
            records = FakeAppliancesService.testPowerApplianceRecords,
            onAdd = {},
            onDelete = {},
            onView = {},
            onEdit = {},
            onSelectAppliance = {},
        )
    }
}
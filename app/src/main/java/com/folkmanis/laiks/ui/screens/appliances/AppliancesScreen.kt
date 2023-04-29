package com.folkmanis.laiks.ui.screens.appliances

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.folkmanis.laiks.model.PowerAppliance


@Composable
internal fun AppliancesScreen(
    appliances: List<PowerAppliance>,
    modifier: Modifier = Modifier,
    onEdit: (id: String) -> Unit = {},
    onAdd: () -> Unit = {},
    onDelete: (String) -> Unit = {},
) {

    Box(modifier = modifier.fillMaxSize()) {

        LazyColumn {
            items(appliances, key = { it.id }) { appliance ->

                ApplianceRow(
                    appliance = appliance,
                    onEdit = onEdit,
                    onDelete = onDelete,
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
            appliances = FakeAppliancesService.testAppliances
        )
    }
}
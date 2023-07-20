package com.folkmanis.laiks.ui.screens.user_settings.appliances

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.folkmanis.laiks.utilities.composables.ListDivider


@Composable
internal fun AppliancesScreen(
    appliances: List<PowerAppliance>,
    modifier: Modifier = Modifier,
    onEdit: (idx: Int) -> Unit,
    onDelete: (idx: Int) -> Unit,
    onAdd:()->Unit,
    onSelectAppliance: (idx: Int) -> Unit,
    busy: Boolean,
) {

    Box(modifier = modifier.fillMaxSize()) {

        LazyColumn {
            itemsIndexed(
                items = appliances,
                key = {_,appliance-> appliance.name }
            )
            {idx, appliance ->

                ApplianceRow(
                    appliance = appliance,
                    onEdit = { onEdit(idx) },
                    onDelete = { onDelete(idx) },
                    modifier = Modifier
                        .clickable { onSelectAppliance(idx) },
                    busy = busy,
                )

                ListDivider()

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
            onDelete = {},
            onEdit = {},
            onSelectAppliance = {},
            onAdd = {},
            appliances = FakeAppliancesService.testAppliances,
            busy = false,
        )
    }
}
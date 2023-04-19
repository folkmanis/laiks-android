@file:OptIn(ExperimentalMaterial3Api::class)

package com.folkmanis.laiks.ui.screens.appliances

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
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
    onNavigateBack: () -> Unit = {},
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.appliances_screen),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAdd,
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            items(appliances, key = { it.id }) { appliance ->

                ApplianceRow(
                    appliance = appliance,
                    onEdit = onEdit,
                    onDelete = onDelete,
                )
                Divider(thickness = 2.dp)

            }
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
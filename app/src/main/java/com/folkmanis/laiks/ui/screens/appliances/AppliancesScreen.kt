package com.folkmanis.laiks.ui.screens.appliances

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.folkmanis.laiks.model.PowerAppliance


@Composable
fun AppliancesScreen(
    modifier: Modifier = Modifier,
    onEdit: (id: String) -> Unit = {},
    onAdd: () -> Unit = {},
    viewModel: AppliancesViewModel = hiltViewModel()
) {

    val appliances by viewModel.appliances.collectAsStateWithLifecycle(initialValue = emptyList())

    Box(modifier = modifier.fillMaxSize()) {

        LazyColumn {
            items(appliances, key = { it.id }) { appliance ->

                ApplianceRow(
                    appliance = appliance,
                    onEdit = onEdit,
                    onDelete = {
                        viewModel.delete(it)
                    }
                )

                Divider(thickness = 2.dp)

            }
        }
    }

}


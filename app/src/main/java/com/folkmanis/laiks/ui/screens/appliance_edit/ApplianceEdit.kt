package com.folkmanis.laiks.ui.screens.appliance_edit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.folkmanis.laiks.R
import com.folkmanis.laiks.model.PowerAppliance

@Composable
fun ApplianceEdit(
    modifier: Modifier = Modifier,
    viewModel: ApplianceEditViewModel = hiltViewModel()
) {

    val state by viewModel.uiState
        .collectAsStateWithLifecycle(initialValue = ApplianceUiState())

    val appliance by viewModel.appliance
        .collectAsStateWithLifecycle(initialValue = PowerAppliance())

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column {

            TextField(
                value = appliance.name,
                onValueChange = viewModel::setName,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.enabled,
                readOnly = !state.editMode,
                label = {
                    Text(text = stringResource(id = R.string.appliance_name_label))
                }
            )

        }
    }
}


package com.folkmanis.laiks.ui.screens.appliance_edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.folkmanis.laiks.APPLIANCE_ID
import com.folkmanis.laiks.R
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.data.fake.FakePricesService

@Composable
fun ApplianceEdit(
    modifier: Modifier = Modifier,
    viewModel: ApplianceEditViewModel = hiltViewModel()
) {

    val state by viewModel.uiState
        .collectAsStateWithLifecycle(initialValue = ApplianceUiState())

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column {

            TextField(
                value = state.name,
                onValueChange = viewModel::setName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                enabled = state.isEnabled,
                readOnly = !state.editMode,
                singleLine = true,
                label = {
                    Text(text = stringResource(id = R.string.appliance_name_label))
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                isError = !state.isNameValid
            )

            MinimumDelayInput(
                value = state.minimumDelay,
                onValueChange = viewModel::setMinimumDelay,
                modifier = Modifier.padding(8.dp),
                label = {
                    Text(text = stringResource(id = R.string.appliance_minimumDelay_label))
                },
                isError = !state.isMinimumDelayValid
            )

            Text(text = state.toString())

        }
    }
}

@Composable
fun MinimumDelayInput(
    value: Long?,
    onValueChange: (Long?) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable() (() -> Unit)? = null,
    isError: Boolean = false,
) {

    var inputState by remember { mutableStateOf("") }

    if (value != null) {
        inputState = value.toString()
    }

    TextField(
        value = inputState,
        onValueChange = {
            inputState = it
            onValueChange(inputState.toLongOrNull())
        },
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next,
        ),
        label = label,
        isError = isError,
    )

}

@Preview
@Composable
fun ApplianceEditPreview() {

    val viewModel = ApplianceEditViewModel(
        pricesService = FakePricesService(),
        SavedStateHandle(mapOf(APPLIANCE_ID to "12AFE35"))
    )
    viewModel.startEdit()
    MaterialTheme {
        ApplianceEdit(
            viewModel = viewModel
        )
    }
}


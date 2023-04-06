package com.folkmanis.laiks.ui.screens.appliance_edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
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
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::setName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
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
                modifier = Modifier
                    .padding(vertical = 8.dp),
                label = {
                    Text(text = stringResource(id = R.string.appliance_minimumDelay_label))
                },
                isError = !state.isMinimumDelayValid
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(
                        id = R.string.appliance_delay_label
                    ),
                    style = MaterialTheme.typography.bodySmall,
                )
                DelayInput(
                    value = state.delay,
                    onValueChange = viewModel::setDelay,
                    modifier = Modifier
                        .padding(start = 16.dp)
                )
            }

            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.appliance_color_label),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(start = 16.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 48.dp, height = 24.dp)
                            .background(
                                color = Color(state.color.toColorInt()),
                            )
                    )
                    Text(
                        text = state.color,
                        style = MaterialTheme.typography.labelMedium,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier
                            .padding(start = 16.dp)
                    )

                }
            }

            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(
                        id = R.string.appliance_enabled_label
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(start = 16.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 8.dp, start = 24.dp)
                ) {
                    Switch(
                        checked = state.enabled,
                        onCheckedChange = viewModel::setEnabled
                    )
                    Text(
                        text = stringResource(id = R.string.appliance_enabled_check),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .padding(start = 16.dp)
                    )

                }
            }

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

    OutlinedTextField(
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
        SavedStateHandle(mapOf(APPLIANCE_ID to "12AFE34"))
    )
    viewModel.startEdit()
    MaterialTheme {
        ApplianceEdit(
            viewModel = viewModel
        )
    }
}


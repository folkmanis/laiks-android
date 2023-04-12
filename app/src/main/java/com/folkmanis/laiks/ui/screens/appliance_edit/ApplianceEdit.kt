package com.folkmanis.laiks.ui.screens.appliance_edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.folkmanis.laiks.APPLIANCE_ID
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.fake.FakePricesService

@Composable
fun ApplianceEdit(
    modifier: Modifier = Modifier,
    viewModel: ApplianceEditViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
) {

    val state by viewModel.uiState
        .collectAsStateWithLifecycle(initialValue = ApplianceUiState())

    val scroll = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 56.dp)
                .verticalScroll(scroll)
        ) {

            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::setName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                enabled = state.isEnabled,
                singleLine = true,
                label = {
                    Text(text = stringResource(id = R.string.appliance_name_label))
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                isError = !state.isNameValid
            )

            NumberInput(
                value = state.minimumDelay,
                onValueChange = viewModel::setMinimumDelay,
                modifier = Modifier
                    .padding(vertical = 8.dp),
                label = {
                    Text(text = stringResource(id = R.string.appliance_minimumDelay_label))
                },
                isError = !state.isMinimumDelayValid,
                enabled = state.isEnabled,
            )

            OptionWithLabel(
                label = stringResource(id = R.string.appliance_delay_label),
                modifier = Modifier
                    .padding(8.dp)
            ) {
                DelayInput(
                    value = state.delay,
                    onValueChange = viewModel::setDelay,
                    enabled = state.isEnabled,
                )
            }

            OptionWithLabel(
                label = stringResource(id = R.string.appliance_color_label),
                modifier = Modifier
                    .padding(8.dp)
            ) {
                ColorSelection(
                    color = state.color,
                    onColorChange = viewModel::setColor,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 24.dp)
                )
            }

            OptionWithLabel(
                label = stringResource(
                    id = R.string.appliance_enabled_label
                ),
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 8.dp, start = 24.dp)
                ) {
                    Switch(
                        checked = state.enabled,
                        onCheckedChange = viewModel::setEnabled,
                        enabled = state.isEnabled,
                    )
                    Text(
                        text = stringResource(id = R.string.appliance_enabled_check),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .padding(start = 16.dp)
                    )
                }
            }

            OptionWithLabel(
                label = stringResource(
                    id = R.string.appliance_cycles_label
                )
            ) {
                PowerConsumptionCyclesScreen(
                    cycles = state.cycles,
                    onCyclesChange = viewModel::setCycles,
                    enabled = state.isEnabled,
                )
            }


            Text(text = state.toString())

        }

        EditorActions(
            onSave = {
                viewModel.save { onNavigateBack() }
            },
            onCancel = onNavigateBack,
            saveEnabled = state.isValid,
            cancelEnabled = !state.isLoading && !state.isSaving,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                )
        )

    }
}

@Composable
fun EditorActions(
    onSave: () -> Unit,
    onCancel: () -> Unit,
    saveEnabled: Boolean,
    cancelEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
    ) {
        TextButton(
            enabled = cancelEnabled,
            onClick = onCancel,
        ) {
            Text(text = stringResource(id = R.string.action_cancel))
        }

        TextButton(
            enabled = saveEnabled,
            onClick = onSave,
        ) {
            Text(text = stringResource(id = R.string.action_save))
        }
    }

}

@Preview
@Composable
fun ApplianceEditPreview() {

    val viewModel = ApplianceEditViewModel(
        pricesService = FakePricesService(),
        SavedStateHandle(mapOf(APPLIANCE_ID to "12AFE34"))
    )

    MaterialTheme {
        ApplianceEdit(
            viewModel = viewModel,
            onNavigateBack = {},
        )
    }
}


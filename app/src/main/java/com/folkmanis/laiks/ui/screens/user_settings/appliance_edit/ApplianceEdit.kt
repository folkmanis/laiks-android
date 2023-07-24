package com.folkmanis.laiks.ui.screens.user_settings.appliance_edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.fake.FakeAppliancesService
import com.folkmanis.laiks.data.fake.FakeLaiksUserService

@Composable
fun ApplianceEditScreen(
    idx: Int?,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ApplianceEditViewModel = hiltViewModel()
) {

    val state by viewModel.uiState
        .collectAsStateWithLifecycle(initialValue = ApplianceUiState())


    LaunchedEffect(idx) {
        viewModel.loadAppliance(idx)
    }


    Box(
        modifier = modifier
            .fillMaxSize()
    ) {

        ApplianceEdit(
            state = state,
            setName = viewModel::setName,
            setMinimumDelay = viewModel::setMinimumDelay,
            setDelay = viewModel::setDelay,
            setColor = viewModel::setColor,
            setEnabled = viewModel::setEnabled,
            setCycles = viewModel::setCycles,
            modifier = Modifier
                .matchParentSize()
                .padding(bottom = 56.dp),
        )


        Row(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
                .padding(end = 16.dp)
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            EditorActions(
                onSave = { viewModel.save { onNavigateBack() } },
                saveEnabled = state.isValid,
            )
        }
    }

}

@Composable
internal fun ApplianceEdit(
    modifier: Modifier = Modifier,
    state: ApplianceUiState,
    setName: (String) -> Unit,
    setMinimumDelay: (Long?) -> Unit,
    setDelay: (String) -> Unit,
    setColor: (String) -> Unit,
    setEnabled: (Boolean) -> Unit,
    setCycles: (List<NullablePowerApplianceCycle>) -> Unit,
) {

    val scroll = rememberScrollState()

    Column(modifier = modifier) {

        Column(
            modifier = Modifier
                .verticalScroll(scroll)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = state.name,
                onValueChange = setName,
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = state.isEnabled,
                singleLine = true,
                label = {
                    Text(text = stringResource(id = R.string.appliance_name_label))
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Sentences,
                ),
                isError = !state.isNameValid
            )

            Spacer(modifier = Modifier.height(16.dp))

            NumberInput(
                value = state.minimumDelay,
                onValueChange = setMinimumDelay,
                modifier = Modifier
                    .padding(bottom = 8.dp),
                label = {
                    Text(text = stringResource(id = R.string.appliance_minimumDelay_label))
                },
                isError = !state.isMinimumDelayValid,
                enabled = state.isEnabled,
            )

            OptionsDivider()

            OptionWithLabel(
                label = stringResource(id = R.string.appliance_delay_label),
                modifier = Modifier
                    .padding(8.dp)
            ) {
                DelayInput(
                    value = state.delay,
                    onValueChange = setDelay,
                    enabled = state.isEnabled,
                )
            }

            OptionsDivider()

            OptionWithLabel(
                label = stringResource(id = R.string.appliance_color_label),
                modifier = Modifier
                    .padding(8.dp)
            ) {
                ColorSelection(
                    color = state.color,
                    onColorChange = setColor,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 24.dp)
                )
            }

            OptionsDivider()

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
                        onCheckedChange = setEnabled,
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

            OptionsDivider()

            OptionWithLabel(
                label = stringResource(
                    id = R.string.appliance_cycles_label
                )
            ) {
                PowerConsumptionCyclesScreen(
                    cycles = state.cycles,
                    onCyclesChange = setCycles,
                    enabled = state.isEnabled,
                )
            }

        }


    }


}

@Composable
fun EditorActions(
    onSave: () -> Unit,
    saveEnabled: Boolean,

    ) {

    Button(
        enabled = saveEnabled,
        onClick = onSave,
    ) {
        Text(text = stringResource(id = R.string.action_save))
    }
}

@Composable
fun OptionsDivider(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier
            .padding(vertical = 8.dp)
            .height(1.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun ApplianceEditPreview() {

    val viewModel = ApplianceEditViewModel(
        appliancesService = FakeAppliancesService(),
        laiksUserService = FakeLaiksUserService(),
    )

    MaterialTheme {
        ApplianceEditScreen(
            viewModel = viewModel,
            onNavigateBack = {},
            idx = null,
        )
    }
}

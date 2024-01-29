package com.folkmanis.laiks.ui.screens.user_settings.appliance_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.fake.FakeLaiksUserService
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.ui.screens.user_settings.appliance_select.ApplianceSelectDialog

@Composable
fun ApplianceEditScreen(
    idx: Int?,
    onNavigateBack: () -> Unit,
    setTitle: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ApplianceEditViewModel = hiltViewModel()
) {

    val state = viewModel.uiState

    val scroll = rememberScrollState()

    LaunchedEffect(idx) {
        viewModel.loadAppliance(idx)
    }

    val title = composableTitle(state)
    LaunchedEffect(title) {
        setTitle(title)
    }


    Column(
        modifier = modifier,
    ) {

        ActionRow(
            onSave = { viewModel.save { onNavigateBack() } },
            onSetAppliance = { viewModel.setAppliance(it) },
            saveEnabled = state.isValid,
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth(),
        )

        HorizontalDivider()

        ApplianceEditForm(
            state = state,
            setName = viewModel::setName,
            setMinimumDelay = viewModel::setMinimumDelay,
            setDelay = viewModel::setDelay,
            setColor = viewModel::setColor,
            setCycles = viewModel::setCycles,
            modifier = Modifier
                .verticalScroll(scroll),
        )

    }

}

@Composable
internal fun ApplianceEditForm(
    state: ApplianceUiState,
    setName: (String) -> Unit,
    setMinimumDelay: (Long?) -> Unit,
    setDelay: (String) -> Unit,
    setColor: (String) -> Unit,
    setCycles: (List<NullablePowerApplianceCycle>) -> Unit,
    modifier: Modifier = Modifier,
) {


    Column(
        modifier = modifier
            .padding(16.dp)
    ) {

        NameInput(
            name = state.name,
            onNameChange = setName,
            enabled = state.isEnabled,
            isValid = state.isValid,
            color = state.color,
            onColorChange = setColor,
        )

        OptionsDivider()

        OptionWithLabel(
            label = stringResource(id = R.string.appliance_delay_label),
            modifier = Modifier
        ) {

            DelayInput(
                delayType = state.delay,
                onDelayTypeChange = setDelay,
                enabled = state.isEnabled,
                modifier = Modifier.fillMaxWidth(),
                minimumDelay = state.minimumDelay,
                onSetMinimumDelay = setMinimumDelay,
                minimumDelayValid = state.isMinimumDelayValid,
            )
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

@Composable
fun ActionRow(
    onSave: () -> Unit,
    onSetAppliance: (PowerAppliance) -> Unit,
    saveEnabled: Boolean,
    modifier: Modifier = Modifier,
) {

    var loadApplianceDialogOpened by remember {
        mutableStateOf(false)
    }

    if (loadApplianceDialogOpened) {
        ApplianceSelectDialog(
            onDismiss = { loadApplianceDialogOpened = false },
            onSelect = {
                loadApplianceDialogOpened = false
                onSetAppliance(it)
            },
        )
    }


    Row(
        modifier = modifier.padding(end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        TextButton(
            onClick = { loadApplianceDialogOpened = true }
        ) {
            Text(text = stringResource(R.string.copy_from))
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            enabled = saveEnabled,
            onClick = onSave,
        ) {
            Text(text = stringResource(id = R.string.action_save))
        }

    }

}

@Composable
fun OptionsDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        thickness = 1.dp,
        modifier = modifier.padding(vertical = 16.dp)
    )
}

@Composable
fun composableTitle(state: ApplianceUiState): String {
    val defaultTitle = stringResource(id = R.string.appliance_screen)
    val title by remember(state) {
        derivedStateOf {
            state.name.ifBlank {
                defaultTitle
            }
        }
    }
    return title
}

@Preview(showBackground = true)
@Composable
fun ApplianceEditPreview() {

    val viewModel = ApplianceEditViewModel(
        laiksUserService = FakeLaiksUserService(),
    )

    MaterialTheme {
        ApplianceEditScreen(
            setTitle = {},
            viewModel = viewModel,
            onNavigateBack = {},
            idx = null,
        )
    }
}


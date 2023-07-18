package com.folkmanis.laiks.ui.screens.appliance_select

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.fake.FakeAppliancesService
import com.folkmanis.laiks.model.ApplianceType
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.model.PowerApplianceRecord
import com.folkmanis.laiks.utilities.composables.ButtonRow
import com.folkmanis.laiks.utilities.composables.ErrorScreen
import com.folkmanis.laiks.utilities.composables.ListDivider
import com.folkmanis.laiks.utilities.composables.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplianceSelectDialog(
    onDismiss: () -> Unit = {},
    onApplianceSelect: (PowerApplianceRecord) -> Unit,
    onNewAppliance: () -> Unit,
    modifier: Modifier = Modifier,
    selectViewModel: ApplianceSelectViewModel = hiltViewModel(),
) {

    val state by selectViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        selectViewModel.initialize()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
    ) {
        AppliancesSelectScreen(
            state = state,
            onSelect = selectViewModel::selectAppliance,
            onDismiss = onDismiss,
            onAccept = {
                val selected = selectViewModel.selectedValue
                if (selected == null) {
                    onNewAppliance()
                } else {
                    onApplianceSelect(selected)
                }
            }
        )
    }

}

@Composable
fun AppliancesSelectScreen(
    state: ApplianceSelectUiState,
    onSelect: (PowerApplianceRecord?) -> Unit,
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Surface(
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.large,
        tonalElevation = AlertDialogDefaults.TonalElevation,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            Icon(
                painter = painterResource(id = R.drawable.dishwasher_gen),
                contentDescription = null,
                modifier = Modifier.padding(top = 16.dp)
            )

            Text(
                text = stringResource(id = R.string.choose_appliance_title),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp),
            )

            ListDivider()

            when (state) {
                is ApplianceSelectUiState.Loading -> {}
                is ApplianceSelectUiState.Error -> ErrorScreen()
                is ApplianceSelectUiState.Success -> {
                    AppliancesList(
                        userAppliances = state.userAppliances,
                        systemAppliances = state.systemAppliances,
                        selected = state.selected,
                        onSelect = onSelect
                    )
                }
            }

            ButtonRow(
                onDismiss = onDismiss,
                onAccept = onAccept,
                saveEnabled = state is ApplianceSelectUiState.Success,
            )

        }

    }

}

@Composable
fun AppliancesList(
    userAppliances: List<PowerAppliance>,
    systemAppliances: List<PowerAppliance>,
    selected: PowerApplianceRecord?,
    onSelect: (PowerApplianceRecord?) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            ListItem(
                leadingContent = {
                    RadioButton(
                        selected = selected == null,
                        onClick = { onSelect(null) },
                    )
                },
                headlineContent = {
                    Text(text = "<" + stringResource(id = R.string.create_new) + ">")
                },
                modifier = Modifier.clickable { onSelect(null) },
            )
            ListDivider()
        }
        if (userAppliances.isNotEmpty()) {
            item {
                SectionHeader(text = R.string.user_defined)
                ListDivider()
            }
            items(userAppliances) { appliance ->

                ApplianceItem(
                    selected = selected,
                    record = PowerApplianceRecord(appliance, ApplianceType.USER.type),
                    onSelect = onSelect,
                )
                ListDivider()

            }
        }
        if (systemAppliances.isNotEmpty()) {
            item {
                SectionHeader(text = R.string.predefined)
                ListDivider()
            }
            items(systemAppliances) { appliance ->
                ApplianceItem(
                    selected = selected,
                    record = PowerApplianceRecord(appliance, ApplianceType.SYSTEM.type),
                    onSelect = onSelect,
                )
                ListDivider()
            }
        }
    }
}

@Composable
fun SectionHeader(@StringRes text: Int) {
    Row(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(start = 16.dp, end = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = text),
            style = MaterialTheme.typography.titleMedium,
        )
    }

}

@Composable
fun ApplianceItem(
    selected: PowerApplianceRecord?,
    record: PowerApplianceRecord,
    onSelect: (PowerApplianceRecord) -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        leadingContent = {
            RadioButton(
                selected = selected == record,
                onClick = { onSelect(record) }
            )
        },
        headlineContent = {
            Text(text = record.appliance.name)
        },
        modifier = modifier
            .clickable { onSelect(record) },
    )

}

@Preview
@Composable
fun AppliancesSelectScreenPreview() {
    var state by remember {
        mutableStateOf(
            ApplianceSelectUiState.Success(
                userAppliances = FakeAppliancesService.testAppliances,
                systemAppliances = FakeAppliancesService.testAppliances,
                selected = PowerApplianceRecord(
                    appliance = FakeAppliancesService.washer,
                    type = ApplianceType.SYSTEM.type,
                ),
            )

        )
    }

    MaterialTheme {
        AppliancesSelectScreen(
            state = state,
            onSelect = {
                state = state.copy(selected = it)
            },
            onAccept = {},
            onDismiss = {},
        )
    }
}

@Preview
@Composable
fun AppliancesSelectScreenLoadingPreview() {
    MaterialTheme {
        AppliancesSelectScreen(
            state = ApplianceSelectUiState.Loading,
            onSelect = {},
            onAccept = {},
            onDismiss = {},
        )
    }
}

package com.folkmanis.laiks.ui.screens.user_settings.appliance_select

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.fake.FakeAppliancesService
import com.folkmanis.laiks.model.ApplianceType
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.model.PowerApplianceRecord
import com.folkmanis.laiks.utilities.composables.ButtonRow
import com.folkmanis.laiks.utilities.composables.ErrorScreen
import com.folkmanis.laiks.utilities.composables.ListDivider
import com.folkmanis.laiks.utilities.composables.LoadingScreen


@Composable
fun AppliancesSelectScreen(
    onSelect: (PowerApplianceRecord) -> Unit,
    modifier: Modifier = Modifier,
) {

    Surface(
        modifier = modifier.fillMaxSize()
    ) {

        when (state) {
            is ApplianceSelectUiState.Loading -> LoadingScreen()
            is ApplianceSelectUiState.Error -> ErrorScreen()
            is ApplianceSelectUiState.Success -> {
                AppliancesList(
                    userAppliances = state.userAppliances,
                    systemAppliances = state.systemAppliances,
                    onSelect = onSelect,
                    onCopy = onCopy,
                    onCreateNew = onCreateNew,
                )
            }
        }

    }

}

@Composable
fun AppliancesList(
    userAppliances: List<PowerAppliance>,
    systemAppliances: List<PowerAppliance>,
    onSelect: (PowerApplianceRecord) -> Unit,
    onCopy: (PowerApplianceRecord) -> Unit,
    onCreateNew: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            ListItem(
                headlineContent = {
                    Text(text = "<" + stringResource(id = R.string.create_new) + ">")
                },
                modifier = Modifier.clickable(onClick = onCreateNew),
            )
            ListDivider()
        }
        if (userAppliances.isNotEmpty()) {
            item {
                SectionHeader(text = R.string.user_defined)
                ListDivider()
            }
            items(userAppliances) { appliance ->
                val record = PowerApplianceRecord(appliance, ApplianceType.USER.type)
                ApplianceItem(
                    appliance = appliance,
                    onSelect = { onSelect(record) },
                    onCopy = { onCopy(record) }
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
                val record = PowerApplianceRecord(appliance, ApplianceType.SYSTEM.type)
                ApplianceItem(
                    appliance = appliance,
                    onSelect = { onSelect(record) },
                    onCopy = { onCopy(record) }
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
            .height(32.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .padding(horizontal = 16.dp),
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
    appliance: PowerAppliance,
    onSelect: () -> Unit,
    onCopy: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        headlineContent = {
            Text(text = appliance.name)
        },
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = Color(appliance.color.toColorInt()),
                        shape = CircleShape
                    )
            )
        },
        trailingContent = {
            IconButton(onClick = onCopy) {
                Icon(
                    painter = painterResource(R.drawable.content_copy),
                    contentDescription = stringResource(R.string.copy),
                    modifier = Modifier.size(24.dp),
                )
            }
        },
        modifier = modifier
            .clickable(onClick = onSelect),
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
            )

        )
    }

    MaterialTheme {
        AppliancesSelectScreen(
            state = state,
            onSelect = {},
            onCopy = {},
            onCreateNew = {},
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
            onCopy = {},
            onCreateNew = {},
        )
    }
}

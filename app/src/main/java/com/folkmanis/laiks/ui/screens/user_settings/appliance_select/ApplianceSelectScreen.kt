package com.folkmanis.laiks.ui.screens.user_settings.appliance_select

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.fake.FakeAppliancesService
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.utilities.composables.ListDivider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplianceSelectDialog(
    onDismiss: () -> Unit,
    onSelect: (PowerAppliance) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ApplianceSelectViewModel = hiltViewModel(),
) {

    val systemAppliances by viewModel.systemAppliances.collectAsStateWithLifecycle()
    val userAppliances by viewModel.userAppliances.collectAsStateWithLifecycle()

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
    ) {
        AppliancesSelectScreen(
            systemAppliances = systemAppliances,
            userAppliances = userAppliances,
            onSelect = onSelect
        )
    }

}


@Composable
fun AppliancesSelectScreen(
    systemAppliances: List<PowerAppliance>,
    userAppliances: List<PowerAppliance>,
    onSelect: (PowerAppliance) -> Unit,
    modifier: Modifier = Modifier,
) {

    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        AppliancesList(
            userAppliances = userAppliances,
            systemAppliances = systemAppliances,
            onSelect = onSelect,
        )

    }

}

@Composable
fun AppliancesList(
    userAppliances: List<PowerAppliance>,
    systemAppliances: List<PowerAppliance>,
    onSelect: (PowerAppliance) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        if (userAppliances.isNotEmpty()) {
            item {
                SectionHeader(text = R.string.user_defined)
            }
            items(userAppliances) { appliance ->
                ApplianceItem(
                    appliance = appliance,
                    modifier = Modifier.clickable { onSelect(appliance) },
                )
            }
        }
        if (systemAppliances.isNotEmpty()) {
            item {
                SectionHeader(text = R.string.predefined)
            }
            items(systemAppliances) { appliance ->
                ApplianceItem(
                    appliance = appliance,
                    modifier = Modifier.clickable { onSelect(appliance) },
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
        modifier = modifier,
    )

}

@Preview
@Composable
fun AppliancesSelectScreenPreview() {
    val userAppliances = FakeAppliancesService.testAppliances
    val systemAppliances = FakeAppliancesService.testAppliances

    MaterialTheme {
        AppliancesSelectScreen(
            systemAppliances = systemAppliances,
            userAppliances = userAppliances,
            onSelect = {},
        )
    }
}


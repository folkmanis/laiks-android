package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ListItem
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.fake.FakeMarketZonesService
import com.folkmanis.laiks.model.MarketZone
import com.folkmanis.laiks.utilities.composables.ButtonRow
import com.folkmanis.laiks.utilities.composables.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketZoneInputDialog(
    initialZoneId: String,
    onZoneAccept: (MarketZone) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MarketZoneInputViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var currentZoneId by remember {
        mutableStateOf(initialZoneId)
    }

    val saveEnabled by remember(currentZoneId) {
        derivedStateOf {
            currentZoneId != initialZoneId
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
    ) {

        MarketZoneInput(
            currentZoneId = currentZoneId,
            onDismiss = onDismiss,
            onZoneChange = { currentZoneId = it },
            onAccept = onZoneAccept,
            saveEnabled = saveEnabled,
            uiState = uiState,
        )
    }

}

@Composable
fun MarketZoneInput(
    currentZoneId: String,
    onDismiss: () -> Unit,
    onAccept: (MarketZone) -> Unit,
    onZoneChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    saveEnabled: Boolean = false,
    uiState: MarketZonesState,
) {

    Surface(
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.large,
        tonalElevation = AlertDialogDefaults.TonalElevation,
    ) {
        Column(modifier = Modifier) {
            Text(
                text = stringResource(id = R.string.market_zone_select),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp),
            )
            Divider()
            when (uiState) {
                is MarketZonesState.Loading ->
                    LoadingScreen()

                is MarketZonesState.Success -> {
                    LazyColumn {
                        items(uiState.zones, key = { it.id }) { zone ->
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = "${zone.id}, ${zone.description}",
                                        modifier = Modifier
                                            .clickable { onZoneChange(zone.id) }
                                    )
                                },
                                leadingContent = {
                                    RadioButton(
                                        selected = zone.id == currentZoneId,
                                        onClick = { onZoneChange(zone.id) },
                                    )
                                }
                            )
                        }
                    }

                    Divider()
                    ButtonRow(
                        onDismiss = onDismiss,
                        onAccept = {
                            val zone = uiState.zones.find { it.id == currentZoneId }
                            zone?.let { onAccept(it) }
                        },
                        saveEnabled = saveEnabled,
                    )
                }
            }

        }
    }

}

@Preview
@Composable
fun MarketZoneInputDialogPreview() {
    MaterialTheme {
        MarketZoneInputDialog(
            initialZoneId = FakeMarketZonesService.zoneLV.id,
            onZoneAccept = {},
            onDismiss = {},
            viewModel = MarketZoneInputViewModel(FakeMarketZonesService())
        )
    }
}
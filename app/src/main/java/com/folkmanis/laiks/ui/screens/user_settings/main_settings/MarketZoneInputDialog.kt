package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.fake.FakeMarketZonesService
import com.folkmanis.laiks.model.MarketZone
import com.folkmanis.laiks.utilities.composables.DialogWithSaveAndCancel
import com.folkmanis.laiks.utilities.composables.LazyItemSelection
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

    BasicAlertDialog(
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

    DialogWithSaveAndCancel(
        onCancel = onDismiss,
        onSave = {
            val zone = uiState.zones.find { it.id == currentZoneId }
            zone?.let { onAccept(it) }
        },
        saveEnabled = saveEnabled,
        modifier = modifier,
        headingText = R.string.market_zone_select,
    ) {
        if (uiState.loading) {
            LoadingScreen(
                modifier = Modifier.fillMaxHeight(1/3f)
            )
        } else {
            LazyItemSelection(
                data = uiState.zones,
                onItemSelected = onZoneChange,
                selectedId = currentZoneId,
            )
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
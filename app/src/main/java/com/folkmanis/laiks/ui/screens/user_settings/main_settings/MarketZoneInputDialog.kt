package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
    initialZoneId: String?,
    onZoneAccept: (MarketZone) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MarketZoneInputViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(initialZoneId) {
        viewModel.initialize(initialZoneId)
    }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
    ) {

        MarketZoneInput(
            onDismiss = onDismiss,
            onZoneChange = viewModel::setZoneId,
            onAccept = onZoneAccept,
            uiState = uiState,
        )
    }

}

@Composable
fun MarketZoneInput(
    onDismiss: () -> Unit,
    onAccept: (MarketZone) -> Unit,
    onZoneChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    uiState: MarketZonesState,
) {
    when (uiState) {
        is MarketZonesState.Success -> {
            DialogWithSaveAndCancel(
                onCancel = onDismiss,
                onSave = {
                    uiState.getCurrentZone()?.let { onAccept(it) }
                },
                saveEnabled = uiState.saveEnabled,
                modifier = modifier,
                headingText = R.string.market_zone_select,
            ) {
                LazyItemSelection(
                    dataList = uiState.zones,
                    onItemSelected = onZoneChange,
                    selectedId = uiState.currentZoneId,
                )
            }
        }

        is MarketZonesState.Loading -> {
            DialogWithSaveAndCancel(
                onCancel = onDismiss,
                onSave = { },
                headingText = R.string.market_zone_select,
            ) {
                LoadingScreen(
                    modifier = Modifier.fillMaxHeight(1 / 3f)
                )
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
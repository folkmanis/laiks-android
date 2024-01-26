package com.folkmanis.laiks.ui.components.market_zone_dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.fake.FakeMarketZonesService
import com.folkmanis.laiks.model.MarketZone
import com.folkmanis.laiks.utilities.composables.DialogSurface
import com.folkmanis.laiks.utilities.composables.LazyItemSelection
import com.folkmanis.laiks.utilities.composables.LoadingScreen

@Composable
fun MarketZoneDialog(
    onDismiss: () -> Unit,
    onZoneSet: (MarketZone) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MarketZoneDialogViewModel = hiltViewModel(),
) {

    LaunchedEffect(Unit) {
        viewModel.initialize()
    }

    MarketZoneInput(
        onDismiss = onDismiss,
        onZoneChange = { viewModel.setZoneId(it, onZoneSet) },
        uiState = viewModel.uiState,
        modifier = modifier,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketZoneInput(
    onDismiss: () -> Unit,
    onZoneChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    uiState: MarketZoneDialogState,
) {

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
    ) {

    DialogSurface {

        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {

            Text(
                text = stringResource(R.string.market_zone_select),
                modifier = Modifier
                    .padding(16.dp),
                style = MaterialTheme.typography.titleLarge,
            )

            HorizontalDivider()

            if (uiState.zones.isNotEmpty()) {
                LazyItemSelection(
                    dataList = uiState.zones,
                    onItemSelected = onZoneChange,
                    selectedId = uiState.currentZoneId,
                )
            } else
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
        MarketZoneInput(
            onDismiss = {},
            onZoneChange = {},
            uiState = MarketZoneDialogState(
                zones = FakeMarketZonesService.zonesList
            ),
        )
    }
}
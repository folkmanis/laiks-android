package com.folkmanis.laiks.ui.screens.market_zone_select

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.fake.FakeMarketZonesService
import com.folkmanis.laiks.model.MarketZone
import com.folkmanis.laiks.ui.screens.user_settings.appliance_edit.NumberInput
import com.folkmanis.laiks.utilities.composables.LazyItemSelection
import com.folkmanis.laiks.utilities.composables.LoadingScreen
import com.folkmanis.laiks.utilities.modifiers.settingsRow

@Composable
fun MarketZoneSelectScreen(
    modifier: Modifier = Modifier,
    uiState: MarketZoneSelectUiState,
    onMarketZoneSelected: (String) -> Unit,
    onSetVatEnabled: (Boolean) -> Unit,
    onSetVat: (Long?) -> Unit,
    onSave: () -> Unit,
) {

    if (uiState.marketZones != null)
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
        ) {


            MarketZonesList(
                marketZones = uiState.marketZones,
                onMarketZoneSelected = onMarketZoneSelected,
                selectedZoneId = uiState.zoneId,
                modifier = Modifier
                    .weight(1f)
            )

            HorizontalDivider()

            VatAmount(vatAmount = uiState.vatAmount, onSetVat = onSetVat)

            HorizontalDivider()

            VatIncluded(vatEnabled = uiState.vatEnabled, setVatEnabled = onSetVatEnabled)

            HorizontalDivider()

            ActionButtons(onSave = onSave, saveEnabled = uiState.isValid())

        }
    else LoadingScreen()


}

@Composable
fun ActionButtons(
    modifier: Modifier = Modifier,
    onSave: () -> Unit,
    saveEnabled: Boolean,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = modifier.settingsRow(),
    ) {
        FilledTonalButton(onClick = onSave) {
            Text(text = stringResource(R.string.action_save))
        }
    }
}

@Composable
fun VatAmount(
    vatAmount: Long?,
    onSetVat: (Long?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.settingsRow(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NumberInput(
            value = vatAmount,
            onValueChange = onSetVat,
            suffix = { Text(text = "%") },
            modifier = Modifier
                .fillMaxWidth(),
            label = {
                Text(text = stringResource(id = R.string.vat_amount))
            }
        )
    }

}

@Composable
fun VatIncluded(
    vatEnabled: Boolean,
    setVatEnabled: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.settingsRow(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.include_vat),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.weight(1f),
        )
        Switch(
            checked = vatEnabled,
            onCheckedChange = setVatEnabled,
        )
    }

}

@Composable
fun MarketZonesList(
    marketZones: List<MarketZone>,
    onMarketZoneSelected: (String) -> Unit,
    selectedZoneId: String?,
    modifier: Modifier = Modifier,
) {
    LazyItemSelection(
        data = marketZones,
        onItemSelected = onMarketZoneSelected,
        selectedId = selectedZoneId,
        modifier = modifier,
    )
}

@Preview
@Composable
fun MarketZoneSelectScreenPreview() {
    val state = MarketZoneSelectUiState(
        marketZones = FakeMarketZonesService.zonesList
            + (
                (0..50).map { MarketZone(id = "Z$it", description = "Zone $it") }
            ),
        vatEnabled = true,
        zoneId = "LV",
        vatAmount = 21,
    )
    MaterialTheme {
        MarketZoneSelectScreen(
            uiState = state,
            onMarketZoneSelected = {},
            onSetVatEnabled = {},
            onSetVat = {},
            onSave = {},
        )
    }
}

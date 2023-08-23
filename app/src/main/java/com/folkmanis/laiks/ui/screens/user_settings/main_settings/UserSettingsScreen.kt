package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.model.MarketZone
import com.folkmanis.laiks.ui.screens.user_settings.main_settings.UserSettingsUiState.Companion.testUiState
import com.folkmanis.laiks.utilities.ext.toFormattedDecimals

fun Double.toPercentString(): String =
    "${(this * 100).toFormattedDecimals()}%"

fun Modifier.settingsRow(): Modifier =
    fillMaxWidth()
        .height(80.dp)
        .padding(horizontal = 16.dp)


@Composable
fun UserSettingsScreen(
    uiState: UserSettingsUiState.Success,
    onIncludeVatChange: (Boolean) -> Unit,
    onVatChange: (Double) -> Unit,
    onMarketZoneChange: (MarketZone) -> Unit,
    onEditAppliances: ()->Unit,
    modifier: Modifier = Modifier,
) {

    var vatEditorOpen by remember { mutableStateOf(false) }
    var zoneInputOpen by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {

        if (uiState.npUser) {

            Row(
                modifier = Modifier.settingsRow(),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Text(
                    text = stringResource(id = R.string.market_zone_name),
                    style = MaterialTheme.typography.labelLarge,
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = uiState.marketZoneName,
                    style = MaterialTheme.typography.labelLarge,
                )

                EditButton(
                    onClick = { zoneInputOpen = true },
                    modifier = Modifier.padding(start = 8.dp),
                )

            }

            HorizontalDivider()

            Row(
                modifier = Modifier.settingsRow(),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Text(
                    text = stringResource(id = R.string.vat_amount),
                    style = MaterialTheme.typography.labelLarge,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = uiState.vatAmount.toPercentString(),
                    style = MaterialTheme.typography.labelLarge,
                )
                EditButton(
                    onClick = { vatEditorOpen = true },
                    modifier = Modifier.padding(start = 8.dp),
                )

            }

            HorizontalDivider()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.settingsRow(),
            ) {
                Text(stringResource(R.string.include_vat))
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = uiState.includeVat,
                    onCheckedChange = onIncludeVatChange
                )
            }

            HorizontalDivider()

            Row(
                modifier = Modifier.settingsRow(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                FilledTonalButton(
                    onClick = onEditAppliances,
                ) {
                    Text(stringResource(R.string.appliances_menu_item))
                }
            }
        }


    }

    if (vatEditorOpen) {
        VatInputDialog(
            initialValue = (uiState.vatAmount * 100.0).toLong(),
            onVatAccept = { vatUpdate ->
                vatEditorOpen = false
                onVatChange(vatUpdate / 100.0)
            },
            onDismiss = { vatEditorOpen = false })
    }

    if (zoneInputOpen) {
        MarketZoneInputDialog(
            initialZoneId = uiState.marketZoneId,
            onZoneAccept = {
                zoneInputOpen = false
                onMarketZoneChange(it)
            },
            onDismiss = { zoneInputOpen = false }
        )
    }

}

@Composable
fun EditButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedIconButton(
        onClick = onClick,
        modifier = modifier.padding(start = 8.dp),
    ) {
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = stringResource(id = R.string.edit),
            tint = MaterialTheme.colorScheme.primary,
        )
    }

}

@Preview(showBackground = true)
@Composable
fun UserSettingsScreenPreview() {
    var uiState by remember {
        mutableStateOf(testUiState)
    }
    MaterialTheme {
        UserSettingsScreen(
            uiState = uiState,
            onIncludeVatChange = {
                uiState = uiState.copy(includeVat = it)
            },
            onVatChange = {
                uiState = uiState.copy(vatAmount = it)
            },
            onMarketZoneChange = {
                uiState = uiState.copy(marketZoneId = it.id)
            },
            onEditAppliances = {},
        )
    }
}
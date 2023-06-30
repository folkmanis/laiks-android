package com.folkmanis.laiks.ui.screens.user_settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.folkmanis.laiks.R
import com.folkmanis.laiks.model.Locale
import com.folkmanis.laiks.model.MarketZone

@Composable
fun UserSettingsScreen(
    uiState: UserSettingsUiState.Success,
    zones: List<MarketZone>,
    locales: List<Locale>,
    onIncludeVatChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {

        Text(text = uiState.localeName)

        if (uiState.npUser) {

            Text(text = uiState.marketZoneName)

            Text(text = uiState.vatAmount.toString())

            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.include_vat))
                Switch(checked = uiState.includeVat, onCheckedChange = onIncludeVatChange)
            }
        }


    }

}
package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
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
    uiState: UserSettingsUiState,
    onIncludeVatChange: (Boolean) -> Unit,
    onVatChange: (Double) -> Unit,
    onNameChange: (String) -> Unit,
    onMarketZoneChange: (MarketZone) -> Unit,
    onEditAppliances: () -> Unit,
    onDeleteUser: () -> Unit,
    onSendEmailVerification: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val npUser = uiState.npUser

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {

        EmailRow(
            email = uiState.email,
            emailVerified = uiState.emailVerified,
            onSendEmailVerification = onSendEmailVerification,
            modifier = Modifier.settingsRow(),
        )

        HorizontalDivider()

        NameEdit(
            name = uiState.name,
            onNameChange = onNameChange,
            modifier = Modifier.settingsRow(),
        )

        HorizontalDivider()

        if (npUser) {

            MarketZoneEdit(
                marketZoneName = uiState.marketZoneName,
                marketZoneId = uiState.marketZoneId,
                onMarketZoneChange = onMarketZoneChange,
                modifier = Modifier.settingsRow(),
            )

            HorizontalDivider()

            VatEdit(
                modifier = Modifier.settingsRow(),
                onVatChange = onVatChange,
                vatAmount = uiState.vatAmount,
            )

            HorizontalDivider()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.settingsRow(),
            ) {
                Text(
                    stringResource(R.string.include_vat),
                    style = MaterialTheme.typography.labelLarge,
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = uiState.includeVat,
                    onCheckedChange = onIncludeVatChange
                )
            }

        }

        HorizontalDivider()

        Row(
            modifier = Modifier.settingsRow(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {

            DeleteAccountButton(onDelete = onDeleteUser)

            if (npUser) {

                Spacer(modifier = Modifier.width(16.dp))

                FilledTonalButton(
                    onClick = onEditAppliances,
                ) {
                    Text(stringResource(R.string.appliances_menu_item))
                }
            }
        }


    }

}

@Composable
fun NameEdit(
    modifier: Modifier = Modifier,
    name: String,
    onNameChange: (String) -> Unit,
) {

    var nameInputOpen by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelLarge,
        )

        Spacer(modifier = Modifier.weight(1f))

        EditButton(onClick = { nameInputOpen = true })
    }

    if (nameInputOpen) {
        TextInputDialog(
            value = name,
            onValueChange = { newName ->
                nameInputOpen = false
                onNameChange(newName)
            },
            onDismiss = { nameInputOpen = false },
            label = R.string.display_name_input_label
        )
    }

}

@Composable
fun EmailRow(
    modifier: Modifier = Modifier,
    email: String,
    emailVerified: Boolean,
    onSendEmailVerification: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = email,
                color = if (emailVerified)
                    MaterialTheme.colorScheme.onBackground
                else MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelLarge,
            )
            if (!emailVerified) {
                Text(
                    text = stringResource(R.string.email_not_verified),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        if (!emailVerified) {
            OutlinedIconButton(
                onClick = onSendEmailVerification,
                modifier = modifier
                    .padding(start = 8.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = stringResource(R.string.email_verification_button_text),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }

    }
}

@Composable
fun MarketZoneEdit(
    marketZoneName: String,
    marketZoneId: String,
    onMarketZoneChange: (MarketZone) -> Unit,
    modifier: Modifier = Modifier,
) {

    var zoneInputOpen by remember { mutableStateOf(false) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Text(
            text = stringResource(id = R.string.market_zone_name),
            style = MaterialTheme.typography.labelLarge,
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = marketZoneName,
            style = MaterialTheme.typography.labelLarge,
        )

        EditButton(
            onClick = { zoneInputOpen = true },
            modifier = Modifier.padding(start = 8.dp),
        )

    }

    if (zoneInputOpen) {
        MarketZoneInputDialog(
            initialZoneId = marketZoneId,
            onZoneAccept = {
                zoneInputOpen = false
                onMarketZoneChange(it)
            },
            onDismiss = { zoneInputOpen = false }
        )
    }

}

@Composable
fun VatEdit(
    vatAmount: Double,
    onVatChange: (Double) -> Unit,
    modifier: Modifier = Modifier,
) {

    var vatEditorOpen by remember { mutableStateOf(false) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Text(
            text = stringResource(id = R.string.vat_amount),
            style = MaterialTheme.typography.labelLarge,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = vatAmount.toPercentString(),
            style = MaterialTheme.typography.labelLarge,
        )
        EditButton(
            onClick = { vatEditorOpen = true },
            modifier = Modifier.padding(start = 8.dp),
        )

    }

    if (vatEditorOpen) {
        VatInputDialog(
            initialValue = (vatAmount * 100.0).toLong(),
            onVatAccept = { vatUpdate ->
                vatEditorOpen = false
                onVatChange(vatUpdate / 100.0)
            },
            onDismiss = { vatEditorOpen = false })
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
            onNameChange = {},
            onMarketZoneChange = {
                uiState = uiState.copy(marketZoneId = it.id)
            },
            onEditAppliances = {},
            onDeleteUser = {},
            onSendEmailVerification = {},
        )
    }
}
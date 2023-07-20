package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.folkmanis.laiks.R
import com.folkmanis.laiks.ui.screens.user_settings.appliance_edit.NumberInput

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VatInputDialog(
    initialValue: Long,
    onVatAccept: (Long) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var percents by remember {
        mutableStateOf<Long?>(initialValue)
    }

    val saveEnabled by remember(percents, initialValue) {
        derivedStateOf {
            percents != null && percents != initialValue
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier,
    ) {
        VatInputScreen(
            vatPercent = percents,
            onVatChange = { percents = it },
            onDismiss = onDismiss,
            onAccept = {
                val value = percents
                if (value != null) {
                    onVatAccept(value)
                } else {
                    onDismiss()
                }
            },
            saveEnabled = saveEnabled,
        )
    }
}

@Composable
fun VatInputScreen(
    modifier: Modifier = Modifier,
    vatPercent: Long?,
    onVatChange: (Long?) -> Unit,
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
    saveEnabled: Boolean = false,
) {
    Surface(
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.large,
        tonalElevation = AlertDialogDefaults.TonalElevation,
    ) {

        Column(
            modifier = Modifier
        ) {

            Text(
                text = stringResource(id = R.string.vat_amount),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(16.dp)
            )

            Divider()

            NumberInput(
                value = vatPercent,
                onValueChange = onVatChange,
                suffix = { Text(text = "%") },
                modifier = Modifier
                    .padding(16.dp)
            )

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        end = 16.dp,
                        start = 16.dp,
                        bottom = 8.dp
                    )
            ) {
                TextButton(onClick = onDismiss) {
                    Text(text = stringResource(id = R.string.action_cancel))
                }
                TextButton(
                    onClick = onAccept,
                    enabled = saveEnabled,
                ) {
                    Text(text = stringResource(id = R.string.action_save))
                }
            }
        }

    }

}

@Preview
@Composable
fun VatInputDialogPreview() {
    MaterialTheme {
        VatInputDialog(
            initialValue = 21L,
            onDismiss = {},
            onVatAccept = {},
        )
    }
}
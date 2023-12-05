package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.ui.screens.user_settings.appliance_edit.NumberInput
import com.folkmanis.laiks.utilities.composables.DialogWithSaveAndCancel

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

    BasicAlertDialog(
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

    DialogWithSaveAndCancel(
        onCancel = onDismiss,
        onSave = onAccept,
        modifier = modifier,
        saveEnabled = saveEnabled,
        headingText = R.string.vat_amount
    ) {

        NumberInput(
            value = vatPercent,
            onValueChange = onVatChange,
            suffix = { Text(text = "%") },
            modifier = Modifier
                .padding(16.dp)
        )

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
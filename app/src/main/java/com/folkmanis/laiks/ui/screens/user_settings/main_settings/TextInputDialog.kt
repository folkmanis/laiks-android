@file:OptIn(ExperimentalMaterial3Api::class)

package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.utilities.composables.DialogWithSaveAndCancel

@Composable
fun TextInputDialog(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    @StringRes label: Int,
) {

    var updatedValue by remember {
        mutableStateOf(value)
    }

    val saveEnabled = updatedValue.isNotEmpty() && updatedValue != value

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
    ) {
        DialogWithSaveAndCancel(
            onCancel = onDismiss,
            onSave = { onValueChange(updatedValue) },
            saveEnabled = saveEnabled,
            headingText = label,
        ) {
            OutlinedTextField(
                value = updatedValue,
                onValueChange = { update ->
                    updatedValue = update
                },
                label = {
                    Text(text = stringResource(id = label))
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (saveEnabled) onValueChange(updatedValue)
                    }
                )
            )
        }
    }

}

@Preview
@Composable
fun TextInputPreview() {
    MaterialTheme {
        TextInputDialog(
            value = "Example User",
            onValueChange = {},
            onDismiss = {},
            label = R.string.display_name_input_label
        )
    }
}
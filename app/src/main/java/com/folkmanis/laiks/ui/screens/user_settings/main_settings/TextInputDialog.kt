package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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

    DialogWithSaveAndCancel(
        onCancel = onDismiss,
        onSave = { onValueChange(updatedValue) },
        saveEnabled = saveEnabled,
        headingText = label,
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { update ->
                updatedValue = update
            }
        )
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
package com.folkmanis.laiks.utilities.composables

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview

private const val TAG = "NumberDoubleInput"

@Composable
fun NumberDoubleInput(
    value: Double?,
    onValueChange: (Double?) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
) {

    var inputState by remember {
        mutableStateOf(TextFieldValue(""))
    }

    Log.d(TAG, "Composition: $value")

    val valueStr = value?.toString() ?: ""

    if (valueStr != inputState.text) {
        inputState = TextFieldValue(
            text = valueStr,
            selection = TextRange(valueStr.length)
        )
    }

    OutlinedTextField(
        value = inputState,
        onValueChange = {
            inputState = it
            val valueNumerical = inputState.text.toDoubleOrNull()
            onValueChange(valueNumerical)
        },
        modifier = modifier
            .onFocusChanged { focusState ->
                Log.d(TAG, "isFocused: ${focusState.isFocused}")
                if (focusState.isFocused) {
                    inputState = inputState.copy(
                        selection = TextRange(0, inputState.text.length)
                    )
                } else if (inputState.text.toDoubleOrNull() == null) {
                    inputState = TextFieldValue(
                        "0",
                        selection = TextRange(1)
                    )
                    onValueChange(0.0)
                }
            },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next,
        ),
        prefix = prefix,
        suffix = suffix,
        label = label,
        isError = isError,
        enabled = enabled,
    )

}

@Preview
@Composable
fun NumberDoubleInputPreview() {
    var value by remember {
        mutableStateOf<Double?>(5.1)
    }

    MaterialTheme {
        Box {
            NumberDoubleInput(
                value = value,
                onValueChange = { value = it },
                label = { Text("Test") }
            )
        }
    }
}
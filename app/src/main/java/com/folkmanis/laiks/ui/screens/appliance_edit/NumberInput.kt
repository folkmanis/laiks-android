package com.folkmanis.laiks.ui.screens.appliance_edit

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun NumberInput(
    value: Long,
    onValueChange: (Long) -> Unit,
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

    val valueStr = value.toString()

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
            val valueLong = inputState.text.toLongOrNull()
            if (valueLong != null) {
                onValueChange(valueLong)
            }
        },
        modifier = modifier
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    inputState = inputState.copy(
                        selection = TextRange(0, inputState.text.length)
                    )
                } else if (inputState.text.toLongOrNull() == null) {
                    inputState = TextFieldValue(
                        "0",
                        selection = TextRange(1)
                    )
                    onValueChange(0)
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

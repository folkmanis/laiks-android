package com.folkmanis.laiks.utilities.composables

import android.util.Log
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.util.fastJoinToString
import java.text.DecimalFormatSymbols

private const val TAG = "DecimalInputField"

@Composable
fun DecimalInputField(
    value: Double?,
    onValueChange: (Double?) -> Unit,
    modifier: Modifier = Modifier,
    decimalFormatter: DecimalFormatter = DecimalFormatter(),
    label: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
) {

    var inputState by remember {
        mutableStateOf(TextFieldValue(""))
    }

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
            inputState = it.copy(text = decimalFormatter.cleanup(it.text))
            val valueDouble = inputState.text.toDoubleOrNull()
            onValueChange(valueDouble)
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
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Next,
        ),
        prefix = prefix,
        suffix = suffix,
        label = label,
        isError = isError,
        enabled = enabled,
        visualTransformation = DecimalInputVisualTransformation(decimalFormatter),
    )
}

class DecimalFormatter(
    symbols: DecimalFormatSymbols = DecimalFormatSymbols.getInstance()
) {
    private val thousandsSeparator = symbols.groupingSeparator
    private val decimalSeparator = symbols.decimalSeparator

    fun cleanup(input: String): String {
        if (input.matches("\\D".toRegex())) return ""
        if (input.matches("0+".toRegex())) return "0"

        return StringBuilder().apply {
            var hasDecimalSeparator = false
            for (char in input) {
                if (char.isDigit()) {
                    append(char)
                    continue
                }
                if (char == decimalSeparator && !hasDecimalSeparator && isNotEmpty()) {
                    append(char)
                    hasDecimalSeparator = true
                }
            }
        }.toString()
    }

    fun formatForVisual(input: String): String {
        val split = input.split(decimalSeparator)
        val intPart = split[0]
            .reversed()
            .chunked(3)
            .fastJoinToString(separator = thousandsSeparator.toString())
            .reversed()
        val fractionPart = split.getOrNull(1)
        return if (fractionPart == null) intPart else intPart + decimalSeparator + fractionPart
    }
}

class DecimalInputVisualTransformation(private val decimalFormatter: DecimalFormatter) :
    VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val inputText = text.text
        val formattedNumber = decimalFormatter.formatForVisual(inputText)

        val newText = AnnotatedString(
            text = formattedNumber,
            spanStyles = text.spanStyles,
            paragraphStyles = text.paragraphStyles,
        )

        val offsetMapping = FixedCursorOffsetMapping(
            contentLength = inputText.length,
            formattedContentLength = formattedNumber.length
        )

        return TransformedText(newText, offsetMapping)
    }
}

private class FixedCursorOffsetMapping(
    private val contentLength: Int,
    private val formattedContentLength: Int
) : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int = formattedContentLength
    override fun transformedToOriginal(offset: Int): Int = contentLength
}
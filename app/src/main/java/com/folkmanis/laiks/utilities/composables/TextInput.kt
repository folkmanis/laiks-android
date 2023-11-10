package com.folkmanis.laiks.utilities.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.folkmanis.laiks.R

@Composable
fun EmailField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    errorId: (@StringRes Int)? = null,
) {

    val label = stringResource(R.string.email_input_label)

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = true,
        label = { Text(label) },
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = label) },
        keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Email),
        keyboardActions = keyboardActions,
        enabled = enabled,
        isError = errorId != null,
        supportingText = { TextOrEmpty(id = errorId) }
    )
}

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    enabled: Boolean = true,
    errorId: (@StringRes Int)? = null,
) {
    PasswordField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = R.string.password_input_label,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        enabled = enabled,
        errorId = errorId,
    )
}

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes label: Int,
    modifier: Modifier = Modifier,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    enabled: Boolean = true,
    errorId: (@StringRes Int)? = null,
) {
    val labelString = stringResource(label)

    var isVisible by remember {
        mutableStateOf(false)
    }

    val icon =
        if (isVisible) painterResource(R.drawable.visibility_24px)
        else painterResource(R.drawable.visibility_off_24px)

    val visualTransformation =
        if (isVisible) VisualTransformation.None else PasswordVisualTransformation()

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(labelString) },
        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = labelString) },
        trailingIcon = {
            IconButton(onClick = { isVisible = !isVisible }) {
                Icon(
                    painter = icon,
                    contentDescription = stringResource(R.string.password_visibility)
                )
            }
        },
        keyboardOptions = keyboardOptions.copy(
            keyboardType = KeyboardType.Password,
        ),
        visualTransformation = visualTransformation,
        keyboardActions = keyboardActions,
        enabled = enabled,
        isError = errorId != null,
        supportingText = { TextOrEmpty(id = errorId) }
    )
}
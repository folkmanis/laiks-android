package com.folkmanis.laiks.ui.screens.login.user_registration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.ui.theme.LaiksTheme
import com.folkmanis.laiks.utilities.composables.EmailField
import com.folkmanis.laiks.utilities.composables.PasswordField
import com.folkmanis.laiks.utilities.composables.TextOrEmpty

fun Modifier.textInputField(): Modifier =
    this
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)

@Composable
fun UserRegistrationScreen(
    uiState: UserRegistrationUiState,
    modifier: Modifier = Modifier,
    setEmail: (String) -> Unit = {},
    setPassword: (String) -> Unit = {},
    setPasswordRepeat: (String) -> Unit = {},
    setDisplayName: (String) -> Unit = {},
    onRegisterUser: () -> Unit = {},
) {

    val busy = uiState.isBusy
    val valid = uiState.isValid

    val focusManager = LocalFocusManager.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {

        Spacer(modifier = Modifier.weight(1f))

        EmailField(
            value = uiState.email,
            onValueChange = setEmail,
            modifier = Modifier
                .textInputField(),
            enabled = !busy,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Next) }
            ),
            errorId = uiState.emailError,
        )

        OutlinedTextField(
            value = uiState.displayName,
            onValueChange = setDisplayName,
            modifier = Modifier.textInputField(),
            enabled = !busy,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = stringResource(R.string.display_name_input_label)
                )
            },
            label = {
                Text(text = stringResource(R.string.display_name_input_label))
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Words,
                keyboardType = KeyboardType.Text,
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Next) }
            ),
            isError = uiState.displayNameError != null,
            supportingText = { TextOrEmpty(id = uiState.displayNameError) }
        )

        PasswordField(
            value = uiState.password,
            onValueChange = setPassword,
            enabled = !busy,
            modifier = Modifier.textInputField(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Next) }
            ),
            errorId = uiState.passwordError,
        )

        PasswordField(
            value = uiState.passwordRepeat,
            onValueChange = setPasswordRepeat,
            enabled = !busy,
            label = R.string.password_repeat_input_label,
            modifier = Modifier.textInputField(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { if (valid) onRegisterUser() }
            ),
            errorId = uiState.passwordRepeatError
        )

        FilledTonalButton(
            onClick = onRegisterUser,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            enabled = !busy && valid,
        ) {
            Text(text = stringResource(R.string.user_registration_action_button))
        }

        Spacer(modifier = Modifier.weight(2f))
    }

}

val previewState = UserRegistrationUiState(
    isBusy = false,
    email = "user@example.com",
    password = "123456",
    passwordRepeat = "",
    displayName = "Example User",
)

@Preview
@Composable
fun UserRegistrationScreenPreview() {
    LaiksTheme {
        UserRegistrationScreen(uiState = previewState.copy())
    }
}
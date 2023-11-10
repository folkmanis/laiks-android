package com.folkmanis.laiks.ui.screens.login

import android.view.KeyEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.ui.theme.LaiksTheme
import com.folkmanis.laiks.utilities.composables.EmailField
import com.folkmanis.laiks.utilities.composables.ListDivider
import com.folkmanis.laiks.utilities.composables.PasswordField
import com.folkmanis.laiks.utilities.ext.isValidEmail

fun Modifier.fieldModifier(): Modifier {
    return this
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
}

fun Modifier.loginButtonModifier(): Modifier {
    return this
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
}

@Composable
fun LoginScreen(
    email: String,
    password: String,
    busy: Boolean,
    onSetEmail: (String) -> Unit,
    onSetPassword: (String) -> Unit,
    onGoogleLogin: () -> Unit,
    onEmailLogin: () -> Unit,
    onPasswordReset: () -> Unit,
    onRegisterWithEmail: () -> Unit,
    isHorizontal: Boolean,
    modifier: Modifier = Modifier,
) {

    if (isHorizontal) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(R.drawable.clock_clock),
                contentDescription = stringResource(R.string.login_title),
                modifier = Modifier
                    .weight(1f)
                    .padding(32.dp)
                    .fillMaxSize()
            )

            LoginInputPanel(
                email = email,
                password = password,
                busy = busy,
                onSetEmail = onSetEmail,
                onSetPassword = onSetPassword,
                onGoogleLogin = onGoogleLogin,
                onEmailLogin = onEmailLogin,
                onPasswordReset = onPasswordReset,
                onRegisterWithEmail=onRegisterWithEmail,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            )

        }

    } else {

        Column(
            modifier = modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(R.drawable.clock_clock),
                contentDescription = stringResource(R.string.login_title),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .widthIn(min = 48.dp, max = 128.dp)
            )

            LoginInputPanel(
                email = email,
                password = password,
                busy = busy,
                onSetEmail = onSetEmail,
                onSetPassword = onSetPassword,
                onGoogleLogin = onGoogleLogin,
                onEmailLogin = onEmailLogin,
                onPasswordReset = onPasswordReset,
                onRegisterWithEmail=onRegisterWithEmail,
            )

            Spacer(modifier = Modifier.weight(2f))

        }

    }

}

@Composable
fun LoginInputPanel(
    email: String,
    password: String,
    busy: Boolean,
    onSetEmail: (String) -> Unit,
    onSetPassword: (String) -> Unit,
    onGoogleLogin: () -> Unit,
    onEmailLogin: () -> Unit,
    onPasswordReset: () -> Unit,
    onRegisterWithEmail: () -> Unit,
    modifier: Modifier = Modifier
) {


    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        EmailField(
            value = email,
            onValueChange = onSetEmail,
            modifier = Modifier.fieldModifier(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            enabled = !busy,
        )

        PasswordField(
            value = password,
            onValueChange = onSetPassword,
            modifier = Modifier
                .fieldModifier()
                .onKeyEvent {
                    if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                        onEmailLogin()
                        true
                    } else false
                },
            keyboardActions = KeyboardActions(onDone = { onEmailLogin() }),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            ),
            enabled = !busy,
        )

        FilledTonalButton(
            onClick = onEmailLogin,
            modifier = Modifier.loginButtonModifier(),
            enabled = !busy && email.isValidEmail()
        ) {
            Text(text = stringResource(id = R.string.login_email_button))
        }

        TextButton(
            onClick = onPasswordReset,
            enabled = !busy,
        ) {
            Text(text = stringResource(R.string.navigate_to_password_reset))
        }

        TextButton(
            onClick = onRegisterWithEmail,
            enabled = !busy
        ) {
            Text(text = stringResource(R.string.login_register_with_email))
        }

        ListDivider(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        FilledTonalButton(
            onClick = onGoogleLogin,
            enabled = !busy,
            modifier = Modifier.loginButtonModifier()
        ) {
            Text(text = stringResource(id = R.string.login_google_button))
        }

    }
}

@Preview
@Composable
fun LoginScreenVerticalPreview() {
    LaiksTheme {
        LoginScreen(
            email = "user@example.com",
            password = "some_password",
            onSetEmail = {},
            onSetPassword = {},
            onGoogleLogin = { },
            onEmailLogin = { },
            onPasswordReset = {},
            busy = false,
            isHorizontal = false,
            onRegisterWithEmail = {},
        )
    }
}

@Preview(device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun LoginScreenHorizontalPreview() {
    LaiksTheme {
        LoginScreen(
            email = "user@example.com",
            password = "some_password",
            busy = true,
            onSetEmail = {},
            onSetPassword = {},
            onGoogleLogin = { },
            onEmailLogin = { },
            onPasswordReset = {},
            isHorizontal = true,
            onRegisterWithEmail = {},
        )
    }
}

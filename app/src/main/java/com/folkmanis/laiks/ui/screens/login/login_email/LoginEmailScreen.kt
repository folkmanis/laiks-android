package com.folkmanis.laiks.ui.screens.login.login_email

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R
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
fun LoginEmailScreen(
    email: String,
    password: String,
    busy: Boolean,
    onSetEmail: (String) -> Unit,
    onSetPassword: (String) -> Unit,
    onEmailLogin: () -> Unit,
    onPasswordReset: () -> Unit,
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
                    .fillMaxSize(),
                colorFilter = ColorFilter
                    .tint(MaterialTheme.colorScheme.primary),
            )

            EmailInputPanel(
                email = email,
                password = password,
                busy = busy,
                onSetEmail = onSetEmail,
                onSetPassword = onSetPassword,
                onEmailLogin = onEmailLogin,
                onPasswordReset = onPasswordReset,
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
                    .widthIn(min = 48.dp, max = 128.dp),
                colorFilter = ColorFilter
                    .tint(MaterialTheme.colorScheme.primary),
            )

            EmailInputPanel(
                email = email,
                password = password,
                busy = busy,
                onSetEmail = onSetEmail,
                onSetPassword = onSetPassword,
                onEmailLogin = onEmailLogin,
                onPasswordReset = onPasswordReset,
            )

            Spacer(modifier = Modifier.weight(2f))

        }

    }

}

@Composable
fun EmailInputPanel(
    email: String,
    password: String,
    busy: Boolean,
    onSetEmail: (String) -> Unit,
    onSetPassword: (String) -> Unit,
    onEmailLogin: () -> Unit,
    onPasswordReset: () -> Unit,
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

        ListDivider(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        TextButton(
            onClick = onPasswordReset,
            enabled = !busy,
        ) {
            Text(text = stringResource(R.string.navigate_to_password_reset))
        }

    }
}

@Preview(device = "spec:parent=pixel_5")
@Composable
fun LoginEmailScreenPreview() {
    MaterialTheme {
        LoginEmailScreen(
            email = "user@example.com",
            password = "12345678",
            busy = false,
            onSetEmail = {},
            onSetPassword = {},
            onEmailLogin = {},
            onPasswordReset = {},
            isHorizontal = false
        )
    }
}

@Preview(device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun LoginEmailScreenHorizontalPreview() {
    MaterialTheme {
        LoginEmailScreen(
            email = "user@example.com",
            password = "12345678",
            busy = false,
            onSetEmail = {},
            onSetPassword = {},
            onEmailLogin = {},
            onPasswordReset = {},
            isHorizontal = true
        )
    }
}
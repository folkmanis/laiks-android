package com.folkmanis.laiks.ui.screens.login

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.view.KeyEvent
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.ui.theme.LaiksTheme
import com.folkmanis.laiks.utilities.composables.EmailField
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
    onSetEmail: (String) -> Unit,
    onSetPassword: (String) -> Unit,
    onGoogleLogin: () -> Unit,
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
                    .fillMaxSize()
            )

            LoginInputPanel(
                email = email,
                password = password,
                onSetEmail = onSetEmail,
                onSetPassword = onSetPassword,
                onGoogleLogin = onGoogleLogin,
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
                    .widthIn(min = 48.dp, max = 128.dp)
            )

            LoginInputPanel(
                email = email,
                password = password,
                onSetEmail = onSetEmail,
                onSetPassword = onSetPassword,
                onGoogleLogin = onGoogleLogin,
                onEmailLogin = onEmailLogin,
                onPasswordReset = onPasswordReset,
            )

            Spacer(modifier = Modifier.weight(2f))

        }

    }

}

@Composable
fun LoginInputPanel(
    email: String,
    password: String,
    onSetEmail: (String) -> Unit,
    onSetPassword: (String) -> Unit,
    onGoogleLogin: () -> Unit,
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
            modifier = Modifier.fieldModifier()
        )

        PasswordField(
            value = password,
            onValueChange = onSetPassword,
            modifier = Modifier
                .fieldModifier(),
//                .onKeyEvent {
//                    if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
//                        onEmailLogin()
//                    }
//                   return@onKeyEvent true
//                },
            keyboardActions = KeyboardActions(onDone = { onEmailLogin() }),
        )

        FilledTonalButton(
            onClick = onEmailLogin,
            modifier = Modifier.loginButtonModifier(),
            enabled = email.isValidEmail()
        ) {
            Text(text = stringResource(id = R.string.login_email_button))
        }

        TextButton(onClick = onPasswordReset) {
            Text(text = stringResource(R.string.navigate_to_password_reset))
        }

        HorizontalDivider(
            thickness = 2.dp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        FilledTonalButton(
            onClick = onGoogleLogin,
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
            isHorizontal = false,
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
            onSetEmail = {},
            onSetPassword = {},
            onGoogleLogin = { },
            onEmailLogin = { },
            onPasswordReset = {},
            isHorizontal = true,
        )
    }
}
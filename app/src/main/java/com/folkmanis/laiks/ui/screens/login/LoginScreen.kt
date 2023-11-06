package com.folkmanis.laiks.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    modifier: Modifier = Modifier,
) {
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
            modifier = Modifier.fillMaxSize()
        )
        EmailField(
            value = email,
            onValueChange = onSetEmail,
            modifier = Modifier.fieldModifier()
        )

        PasswordField(
            value = password,
            onValueChange = onSetPassword,
            modifier = Modifier.fieldModifier()
        )

        FilledTonalButton(
            onClick = onEmailLogin,
            modifier = Modifier.loginButtonModifier(),
            enabled = email.isValidEmail()
        ) {
            Text(text = stringResource(id = R.string.login_email_button))
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
        Spacer(modifier = Modifier.weight(2f))
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LaiksTheme {
        LoginScreen(
            email = "user@example.com",
            password = "some_password",
            onSetEmail = {},
            onSetPassword = {},
            onGoogleLogin = { },
            onEmailLogin = { }
        )
    }
}
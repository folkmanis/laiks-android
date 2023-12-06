package com.folkmanis.laiks.ui.screens.login.login_select

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.utilities.composables.ListDivider

fun Modifier.loginButtonModifier(): Modifier {
    return this
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
}


@Composable
fun LoginSelectScreen(
    onLoginWithEmail: () -> Unit,
    onLoginWithGoogle: () -> Unit,
    onRegisterWithEmail: () -> Unit,
    onRegisterWithGoogle: () -> Unit,
    isHorizontal: Boolean,
    modifier: Modifier = Modifier,
) {

    if (isHorizontal) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
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

            LoginSelectButtons(
                onLoginWithEmail = onLoginWithEmail,
                onLoginWithGoogle = onLoginWithGoogle,
                onRegisterWithEmail = onRegisterWithEmail,
                onRegisterWithGoogle = onRegisterWithGoogle,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
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
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                colorFilter = ColorFilter
                    .tint(MaterialTheme.colorScheme.primary),
            )

            LoginSelectButtons(
                onLoginWithEmail = onLoginWithEmail,
                onLoginWithGoogle = onLoginWithGoogle,
                onRegisterWithEmail = onRegisterWithEmail,
                onRegisterWithGoogle = onRegisterWithGoogle
            )

            Spacer(modifier = Modifier.weight(2f))

        }
    }

}

@Composable
fun LoginSelectButtons(
    onLoginWithEmail: () -> Unit,
    onLoginWithGoogle: () -> Unit,
    onRegisterWithEmail: () -> Unit,
    onRegisterWithGoogle: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        FilledTonalButton(
            onClick = onLoginWithEmail,
            modifier = Modifier.loginButtonModifier(),
        ) {
            Text(text = stringResource(R.string.login_email_button))
        }

        FilledTonalButton(
            onClick = onLoginWithGoogle,
            modifier = Modifier.loginButtonModifier(),
        ) {
            Text(text = stringResource(R.string.login_google_button))
        }

        ListDivider(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        TextButton(
            onClick = onRegisterWithEmail,
        ) {
            Text(text = stringResource(R.string.login_register_with_email))
        }

        TextButton(
            onClick = onRegisterWithGoogle
        ) {
            Text(text = stringResource(R.string.login_register_with_google))
        }

    }
}

@Preview(device = "spec:parent=pixel_5")
@Composable
fun LoginSelectScreenPreview() {
    MaterialTheme {
        LoginSelectScreen(
            onLoginWithEmail = { },
            onLoginWithGoogle = {},
            onRegisterWithEmail = {},
            onRegisterWithGoogle = {},
            isHorizontal = false
        )
    }
}

@Preview(device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun LoginSelectScreenHorizontalPreview() {
    MaterialTheme {
        LoginSelectScreen(
            onLoginWithEmail = {},
            onLoginWithGoogle = {},
            onRegisterWithEmail = {},
            onRegisterWithGoogle = {},
            isHorizontal = true
        )
    }
}
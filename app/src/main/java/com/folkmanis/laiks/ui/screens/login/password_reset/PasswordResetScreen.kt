package com.folkmanis.laiks.ui.screens.login.password_reset

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.ui.theme.LaiksTheme
import com.folkmanis.laiks.utilities.composables.EmailField
import com.folkmanis.laiks.utilities.ext.isValidEmail

@Composable
fun PasswordResetScreen(
    onReset: () -> Unit,
    email: String,
    setEmail: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
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

        EmailField(
            value = email,
            onValueChange = setEmail,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            enabled = enabled,

            )

        FilledTonalButton(
            onClick = onReset,
            enabled = email.isValidEmail() && enabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(text = stringResource(R.string.password_reset_button))
        }

        Spacer(modifier = Modifier.weight(2f))
    }
}

@Preview
@Composable
fun PasswordResetScreenPreview() {
    LaiksTheme {
        PasswordResetScreen(
            onReset = { },
            email = "user@example.com",
            setEmail = {},
            enabled = false,
        )
    }
}
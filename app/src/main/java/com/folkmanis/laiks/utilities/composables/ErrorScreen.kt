package com.folkmanis.laiks.utilities.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    reason: String? = null,
    @DrawableRes icon: Int = R.drawable.ic_connection_error
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null
        )
        Text(
            text = stringResource(R.string.loading_failed),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (reason != null) {
            Text(
                text = reason,
                modifier = Modifier
                    .padding(horizontal = 32.dp),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
fun ErrorScreenPreview() {
    MaterialTheme {
        ErrorScreen(reason = "Network error")
    }
}
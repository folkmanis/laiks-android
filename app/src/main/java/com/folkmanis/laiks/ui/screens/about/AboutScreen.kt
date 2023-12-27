package com.folkmanis.laiks.ui.screens.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.folkmanis.laiks.BuildConfig
import com.folkmanis.laiks.R

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painterResource(R.drawable.clock_clock),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(0.5f),
            colorFilter = ColorFilter
                .tint(MaterialTheme.colorScheme.primary),
        )

        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.about_version_name, BuildConfig.VERSION_NAME),
            style = MaterialTheme.typography.labelLarge,
        )

        Text(
            text = stringResource(R.string.about_version_code, BuildConfig.VERSION_CODE),
            style = MaterialTheme.typography.labelLarge,
        )

        Spacer(modifier = Modifier.weight(2f))

    }
}

@Preview
@Composable
fun AboutScreenPreview() {
    MaterialTheme {
        AboutScreen(
            modifier = Modifier.fillMaxSize()
        )
    }
}
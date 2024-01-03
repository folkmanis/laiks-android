package com.folkmanis.laiks.ui.screens.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.BuildConfig
import com.folkmanis.laiks.R

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    isHorizontal: Boolean = false,
) {
    if (isHorizontal) {
        HorizontalAboutScreen(modifier = modifier)
    } else {
        VerticalAboutScreen(modifier = modifier)
    }
}

@Composable
fun VerticalAboutScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(modifier = Modifier.weight(1f))

        ClockImage(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(top = 32.dp)
        )

        Title(modifier = Modifier.padding(top = 32.dp))

        Version(modifier = Modifier.padding(top = 16.dp))

        Disclaimer(
            modifier = Modifier
                .padding(end = 16.dp, start = 16.dp, top = 32.dp)
                .verticalScroll(rememberScrollState())
        )

        Spacer(modifier = Modifier.weight(2f))

    }
}

@Composable
fun HorizontalAboutScreen(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ClockImage(
            modifier = Modifier
                .weight(2f)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(3f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            Title(modifier = Modifier.padding(top = 64.dp))

            Version(modifier = Modifier.padding(top = 16.dp))

            Disclaimer(
                modifier = Modifier.padding(
                    end = 16.dp, start = 16.dp, top = 32.dp
                )
            )
        }


    }
}

@Composable
fun Title(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(R.string.app_name),
        style = MaterialTheme.typography.headlineLarge,
        modifier = modifier
    )
}

@Composable
fun Version(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.about_version_name, BuildConfig.VERSION_NAME),
            style = MaterialTheme.typography.labelLarge,
        )

        Text(
            text = stringResource(R.string.about_version_code, BuildConfig.VERSION_CODE),
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Composable
fun ClockImage(
    modifier: Modifier = Modifier
) {
    Image(
        painterResource(R.drawable.clock_clock),
        contentDescription = null,
        modifier = modifier,
        colorFilter = ColorFilter
            .tint(MaterialTheme.colorScheme.primary),
    )

}

@Composable
fun Disclaimer(
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.disclaimer_title),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.disclaimer),
            modifier = Modifier
                .width(300.dp),
            textAlign = TextAlign.Justify
        )
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

@Preview(device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun AboutScreenHorizontalPreview() {
    MaterialTheme {
        AboutScreen(
            modifier = Modifier.fillMaxSize(),
            isHorizontal = true,
        )
    }
}
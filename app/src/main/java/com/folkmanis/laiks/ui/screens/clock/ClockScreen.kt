package com.folkmanis.laiks.ui.screens.clock

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.folkmanis.laiks.R
import com.folkmanis.laiks.STEP_DOWN_VALUE
import com.folkmanis.laiks.STEP_UP_VALUE
import com.folkmanis.laiks.data.FakeUserPreferencesRepository
import com.folkmanis.laiks.ui.theme.LaiksTheme
import com.folkmanis.laiks.utilities.ext.hoursString
import com.folkmanis.laiks.utilities.ext.minutesString
import com.folkmanis.laiks.utilities.ext.toSignedString

@Composable
fun ClockScreen(
    pricesAllowed: Boolean,
    onShowPrices: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ClockViewModel = viewModel(
        factory = ClockViewModel.Factory
    ),
) {

    val uiState by viewModel
        .uiState
        .collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
        ) {

            if (pricesAllowed) {
                Button(onClick = onShowPrices) {
                    Text(text = stringResource(id = R.string.show_prices_button))
                }
            }

        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier,
        ) {

            FloatingActionButton(
                onClick = { viewModel.updateOffset(STEP_UP_VALUE) },
                modifier = Modifier
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = stringResource(id = R.string.hour_plus_button)
                )
            }

            Text(
                text = uiState.offset.toSignedString(),
                fontSize = 64.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )

            FloatingActionButton(
                onClick = { viewModel.updateOffset(STEP_DOWN_VALUE) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.remove),
                    contentDescription = stringResource(id = R.string.hour_minus_button)
                )
            }

        }

        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(top = 16.dp),
        ) {
            TimeComponent(
                hours = uiState.time.hoursString,
                minutes = uiState.time.minutesString,
            )
        }


    }

}

@Composable
fun TimeComponent(
    hours: String,
    minutes: String,
    modifier: Modifier = Modifier
) {

    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = { if (it > 0.5) 1f else 0f }),
            repeatMode = RepeatMode.Reverse,
        )
    )

    Row(modifier = modifier) {

        TimeSymbols(text = hours)

        TimeSymbols(
            text = stringResource(id = R.string.minutes_separator),
            modifier = modifier
                .alpha(alpha)
        )

        TimeSymbols(text = minutes)

    }
}

@Composable
fun TimeSymbols(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun LaiksScreenPreview() {
    val viewModel = ClockViewModel(
        FakeUserPreferencesRepository
    )
    LaiksTheme {
        ClockScreen(
            pricesAllowed = true,
            viewModel = viewModel,
            onShowPrices = {},
        )
    }
}

@Preview(showBackground = false)
@Composable
fun LaiksScreenPreviewDark() {
    val viewModel = ClockViewModel(
        FakeUserPreferencesRepository
    )
    LaiksTheme(
        darkTheme = true,
    ) {
        ClockScreen(
            pricesAllowed = true,
            viewModel = viewModel,
            onShowPrices = {},
        )
    }
}

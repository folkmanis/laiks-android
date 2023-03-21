package com.folkmanis.laiks.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.folkmanis.laiks.utilities.timeToHours
import com.folkmanis.laiks.utilities.timeToMinutes

private fun Int.toSignedString(): String {
    return if(this > 0) {
        "+${toString()}"
    } else {
        toString()
    }
}

@Composable
fun ClockScreen(
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

        Spacer(modifier = Modifier.height(16.dp))

        TimeComponent(
            hours = timeToHours(uiState.time),
            minutes = timeToMinutes(uiState.time),
        )

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
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse,
        )
    )

    Row(modifier = modifier) {

        TimeSymbols(text = hours)

        TimeSymbols(
            text = stringResource(id = R.string.minutes_separator),
//            modifier = modifier.alpha(alpha)
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

@Preview
@Composable
fun LaiksScreenPreview() {
    val viewModel = ClockViewModel(
        FakeUserPreferencesRepository
    )
    LaiksTheme {
        ClockScreen(viewModel=viewModel)
    }
}

@Preview
@Composable
fun LaiksScreenPreviewDark() {
    val viewModel = ClockViewModel(
        FakeUserPreferencesRepository
    )
    LaiksTheme(
        darkTheme = true,
    ) {
        ClockScreen(viewModel=viewModel)
    }
}

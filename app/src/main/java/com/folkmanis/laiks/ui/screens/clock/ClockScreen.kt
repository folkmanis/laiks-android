package com.folkmanis.laiks.ui.screens.clock

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
import com.folkmanis.laiks.R
import com.folkmanis.laiks.STEP_DOWN_VALUE
import com.folkmanis.laiks.STEP_UP_VALUE
import com.folkmanis.laiks.data.FakeUserPreferencesRepository
import com.folkmanis.laiks.ui.theme.LaiksTheme
import com.folkmanis.laiks.utilities.ext.hoursString
import com.folkmanis.laiks.utilities.ext.minutesString
import com.folkmanis.laiks.utilities.ext.toSignedString
import androidx.hilt.navigation.compose.hiltViewModel
import com.folkmanis.laiks.data.fake.FakeAccountService

@Composable
fun ClockScreen(
    onShowPrices: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ClockViewModel = hiltViewModel(),
) {

    val uiState by viewModel
        .uiState
        .collectAsStateWithLifecycle()

    val pricesAllowed by viewModel
        .isPricesAllowed
        .collectAsStateWithLifecycle(initialValue = false)

    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {

        ClockSelector(
            offset = uiState.offset,
            time = uiState.time,
            onOffsetChange = viewModel::updateOffset
        )
        
        if (pricesAllowed) {
            FloatingActionButton(
                onClick = onShowPrices,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.euro_symbol_48px),
                    contentDescription = stringResource(id = R.string.show_prices_button),
                    modifier = Modifier.size(24.dp)
                )
            }

        }

    }

}

@Preview(showBackground = true)
@Composable
fun LaiksScreenPreview() {
    val viewModel = ClockViewModel(
        FakeUserPreferencesRepository,
        FakeAccountService()
    )
    LaiksTheme {
        ClockScreen(
            viewModel = viewModel,
            onShowPrices = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LaiksScreenPreviewDark() {
    val viewModel = ClockViewModel(
        FakeUserPreferencesRepository,
        FakeAccountService()
    )
    LaiksTheme(
        darkTheme = true,
    ) {
        ClockScreen(
            viewModel = viewModel,
            onShowPrices = {},
        )
    }
}

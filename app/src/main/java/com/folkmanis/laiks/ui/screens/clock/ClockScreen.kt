@file:OptIn(ExperimentalMaterial3Api::class)

package com.folkmanis.laiks.ui.screens.clock

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.ui.theme.LaiksTheme

@Composable
internal fun ClockScreen(
    uiState: ClockUiState,
    pricesAllowed: Boolean,
    updateOffset: (Int) -> Unit,
    onShowPrices: () -> Unit,
    onNpPrices: ()->Unit ={},
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit,
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.app_name),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = actions,
            )
        },
        modifier = modifier

    ) { innerPadding ->

        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {

            Button(onClick = onNpPrices) {
                Text(text = "Load Np Prices")
            }

            ClockSelector(
                offset = uiState.offset,
                time = uiState.time,
                onOffsetChange = updateOffset
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
}

@Preview(showBackground = true)
@Composable
fun ClockScreenPreview() {
    LaiksTheme {
        ClockScreen(
            onShowPrices = {},
            pricesAllowed = true,
            uiState = ClockUiState(),
            updateOffset = {},
            actions = {},
        )
    }
}


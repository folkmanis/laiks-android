@file:OptIn(ExperimentalMaterial3Api::class)

package com.folkmanis.laiks.ui.screens.prices

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.model.PowerHour
import com.folkmanis.laiks.utilities.composables.ErrorScreen
import com.folkmanis.laiks.utilities.composables.LoadingScreen
import java.time.LocalDate

@Composable
fun PricesScreen(
    state: PricesUiState,
    statistics: PricesStatistics,
    actions: @Composable RowScope.() -> Unit,
    popUp: () -> Unit,
    npUploadAllowed: Boolean,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.prices_screen),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = popUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                },
                actions = actions,
            )
        },
        modifier = modifier,
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            if (npUploadAllowed) {
                FloatingActionButton(onClick = onRefresh) {
                    Icon(imageVector = Icons.Filled.Refresh, contentDescription = null)
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->

        when (state) {
            is PricesUiState.Success -> PricesList(
                groupedCosts = state.groupedCosts,
                statistics = statistics,
                modifier = Modifier.padding(innerPadding),
            )

            is PricesUiState.Loading -> LoadingScreen(
                modifier = Modifier.padding(innerPadding),
            )

            is PricesUiState.Error -> ErrorScreen(
                reason = state.reason,
                modifier = Modifier.padding(innerPadding),
            )
        }

    }


}


@Composable
fun PricesList(
    groupedCosts: Map<LocalDate, List<PowerHour>>,
    statistics: PricesStatistics,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
    ) {
        groupedCosts.forEach { (date, powerHour) ->
            item {
                DateHeader(date = date)
                Divider(
                    thickness = 2.dp,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
            }
            items(powerHour, key = { it.id }) {
                PriceRow(
                    powerHour = it,
                    statistics = statistics,
                    modifier = Modifier
                        .fillMaxWidth(),
                )
                Divider(
                    thickness = 2.dp,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }

}

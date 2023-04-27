package com.folkmanis.laiks.ui.screens.appliance_costs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.model.ApplianceHourWithCosts
import com.folkmanis.laiks.model.PricesStatistics
import com.folkmanis.laiks.utilities.composables.PriceRow
import com.folkmanis.laiks.utilities.composables.ErrorScreen
import com.folkmanis.laiks.utilities.composables.LoadingScreen
import java.time.LocalDateTime
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplianceCostsScreen(
    state: ApplianceCostsUiState,
    statistics: PricesStatistics?,
    snackbarHostState: SnackbarHostState,
    actions: @Composable RowScope.() -> Unit,
    popUp: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val name = when (state) {
        is ApplianceCostsUiState.Success -> state.name
        else -> stringResource(id = R.string.appliance_screen)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = name)
                },
                actions = actions,
                navigationIcon = {
                    IconButton(onClick = popUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {

            when (state) {
                is ApplianceCostsUiState.Loading -> LoadingScreen()
                is ApplianceCostsUiState.Error -> ErrorScreen()
                is ApplianceCostsUiState.Success -> {
                    ApplianceCostsList(
                        costs = state.hoursWithCosts,
                        statistics = statistics
                    )
                }
            }

        }
    }

}

@Composable
fun ApplianceCostsList(
    costs: List<ApplianceHourWithCosts>,
    statistics: PricesStatistics?,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(costs) { cost ->
            val offset = cost.offset
            PriceRow(
                startTime = cost.startTime.toLocalTime(),
                endTime = cost.endTime.toLocalTime(),
                value = cost.value,
                statistics = statistics,
                modifier = Modifier
                    .fillMaxWidth(),
                offset = offset,
            )
            Divider(
                thickness = 2.dp,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
        }

    }
}


@Preview
@Composable
fun ApplianceCostsScreenPreview() {

    val startTime = LocalDateTime.now()
    val hours = (0L..30L).shuffled().map { idx ->
        ApplianceHourWithCosts(
            startTime = startTime.plusHours(idx),
            endTime = startTime.plusHours(idx + 1),
            value = 12.5 * Random.nextDouble(0.5, 1.5),
            offset = idx.toInt(),
        )
    }.sortedBy { it.value }

    ApplianceCostsScreen(
        state = ApplianceCostsUiState.Success(
            hours,
            name = "Veļasmašīna"
        ),
        statistics = PricesStatistics(10.5, 5.2),
        snackbarHostState = SnackbarHostState(),
        actions = {},
        popUp = { }
    )
}
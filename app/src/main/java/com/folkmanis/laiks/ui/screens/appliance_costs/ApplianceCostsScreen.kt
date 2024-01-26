package com.folkmanis.laiks.ui.screens.appliance_costs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.model.ApplianceHourWithCosts
import com.folkmanis.laiks.model.PricesStatistics
import com.folkmanis.laiks.ui.components.market_zone_dialog.MarketZoneDialog
import com.folkmanis.laiks.utilities.composables.ErrorScreen
import com.folkmanis.laiks.utilities.composables.LoadingScreen
import com.folkmanis.laiks.utilities.composables.PriceRow
import java.time.LocalDateTime
import kotlin.random.Random

@Composable
fun ApplianceCostsScreen(
    state: ApplianceCostsUiState,
    modifier: Modifier = Modifier,
    onMarketZoneNotSet: () -> Unit,
    onMarketZoneSet: () -> Unit,
) {


    Box(modifier = modifier) {

        when (state) {
            is ApplianceCostsUiState.Loading -> {
                LoadingScreen()
            }

            is ApplianceCostsUiState.Error -> ErrorScreen()
            is ApplianceCostsUiState.Success -> {
                ApplianceCostsList(
                    costs = state.hoursWithCosts,
                    statistics = state.statistics
                )
            }

            is ApplianceCostsUiState.MarketZoneMissing -> {
                MarketZoneDialog(
                    onDismiss = onMarketZoneNotSet,
                    onZoneSet = { onMarketZoneSet() })
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
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                offset = offset,
            )
            HorizontalDivider(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                thickness = 2.dp
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
    val statistics = PricesStatistics(10.5, 5.2)

    ApplianceCostsScreen(
        state = ApplianceCostsUiState.Success(
            hoursWithCosts = hours, statistics = statistics, name = "", marketZoneId = "LV"
        ),
        onMarketZoneNotSet = {},
        onMarketZoneSet = {},
    )
}
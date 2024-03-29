package com.folkmanis.laiks.ui.screens.prices

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.model.MarketZone
import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.PowerApplianceHour
import com.folkmanis.laiks.model.PricesStatistics
import com.folkmanis.laiks.ui.components.market_zone_dialog.MarketZoneDialog
import com.folkmanis.laiks.utilities.composables.ErrorScreen
import com.folkmanis.laiks.utilities.composables.LoadingScreen
import com.folkmanis.laiks.utilities.composables.PriceRow
import com.folkmanis.laiks.utilities.ext.hoursFrom
import com.folkmanis.laiks.utilities.ext.toLocalTime
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalDateTime

@Suppress("unused")
private const val TAG = "PricesScreen"

@Composable
fun PricesScreen(
    state: PricesUiState,
    statistics: PricesStatistics?,
    appliances: Map<Int, List<PowerApplianceHour>>,
    onMarketZoneSet: (MarketZone) -> Unit,
    onMarketZoneNotSet: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Box(modifier = modifier.fillMaxSize()) {

        when (state) {
            is PricesUiState.Success -> PricesList(
                groupedPrices = state.groupedPrices,
                hour = state.hour,
                currentOffsetIndex = state.currentOffsetIndex,
                statistics = statistics,
                appliances = appliances,
            )

            is PricesUiState.Loading -> LoadingScreen()

            is PricesUiState.Error -> ErrorScreen(
                reason = state.reason,
            )

            is PricesUiState.MarketZoneMissing -> {
                LoadingScreen()
                MarketZoneDialog(
                    onDismiss = onMarketZoneNotSet,
                    onZoneSet = onMarketZoneSet
                )
            }
        }

    }
}


@Composable
fun PricesList(
    groupedPrices: Map<LocalDate, List<NpPrice>>,
    currentOffsetIndex: Int,
    appliances: Map<Int, List<PowerApplianceHour>>,
    hour: LocalDateTime,
    statistics: PricesStatistics?,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(groupedPrices, currentOffsetIndex) {
        delay(500)
        listState.animateScrollToItem(currentOffsetIndex, -15)
    }

    LazyColumn(
        modifier = modifier,
        state = listState,
    ) {
        groupedPrices.forEach { (date, npPrices) ->
            item {
                DateHeaderScreen(date = date)
            }
            itemsIndexed(npPrices, key = { _, price -> price.id }) { idx, npPrice ->
                val offset = npPrice.startTime.hoursFrom(hour)
                if (idx != 0)
                    HorizontalDivider(
                        thickness = 2.dp,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                PriceRow(
                    startTime = npPrice.startTime.toLocalTime(),
                    endTime = npPrice.endTime.toLocalTime(),
                    value = npPrice.value,
                    statistics = statistics,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    offset = offset,
                    disabled = offset < 0,
                    list = {
                        AppliancesCosts(appliances = appliances.getOrDefault(offset, emptyList()))
                    }
                )
            }
        }
    }

}

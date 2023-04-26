@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.folkmanis.laiks.ui.screens.prices

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.PowerApplianceHour
import com.folkmanis.laiks.model.PricesStatistics
import com.folkmanis.laiks.utilities.composables.ErrorScreen
import com.folkmanis.laiks.utilities.composables.LoadingScreen
import com.folkmanis.laiks.utilities.composables.PriceRow
import com.folkmanis.laiks.utilities.ext.hoursFrom
import com.folkmanis.laiks.utilities.ext.toLocalTime
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PricesScreen(
    state: PricesUiState,
    statistics: PricesStatistics?,
    appliances: Map<Int, List<PowerApplianceHour>>,
    actions: @Composable RowScope.() -> Unit,
    popUp: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
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
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->

        when (state) {
            is PricesUiState.Success -> PricesList(
                groupedPrices = state.groupedPrices,
                hour = state.hour,
                statistics = statistics,
                modifier = Modifier.padding(innerPadding),
                appliances = appliances,
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
    groupedPrices: Map<LocalDate, List<NpPrice>>,
    appliances: Map<Int, List<PowerApplianceHour>>,
    hour: LocalDateTime,
    statistics: PricesStatistics?,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
    ) {
        groupedPrices.forEach { (date, npPrices) ->
            item {
                DateHeaderScreen(date = date)
                Divider(
                    thickness = 2.dp,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
            }
            items(npPrices, key = { it.id }) { npPrice ->
                val offset = npPrice.startTime.hoursFrom(hour)
                PriceRow(
                    startTime = npPrice.startTime.toLocalTime(),
                    endTime = npPrice.endTime.toLocalTime(),
                    value = npPrice.value,
                    statistics = statistics,
                    modifier = Modifier
                        .fillMaxWidth(),
                    offset = offset,
                    list = {
                        AppliancesCosts(appliances = appliances.getOrDefault(offset, emptyList()))
                    }
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

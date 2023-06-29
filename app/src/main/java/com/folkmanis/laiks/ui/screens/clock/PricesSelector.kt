package com.folkmanis.laiks.ui.screens.clock

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.domain.PowerApplianceRecord

fun Modifier.clickActive(enabled: Boolean, onClick: () -> Unit) =
    if (enabled)
        this.clickable(onClick = onClick)
    else
        this

@Composable
fun BoxScope.PricesSelector(
    appliances: List<PowerApplianceRecord>,
    onSelected: (PowerApplianceRecord) -> Unit,
    onShowPrices: () -> Unit,
    modifier: Modifier = Modifier,
) {

    var active by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(if (active) 0.8f else 0f)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(alpha = alpha)
            .background(
                color = MaterialTheme
                    .colorScheme
                    .surface
            )
            .clickActive(enabled = active) { active = false }
    )

    Column(
        horizontalAlignment = Alignment.End,
        modifier = modifier
            .padding(bottom = 16.dp)
            .align(Alignment.BottomEnd),
        verticalArrangement = Arrangement.Bottom,
    ) {

        AnimatedVisibility(
            visible = active,
            enter = slideInHorizontally { it },
            exit = slideOutHorizontally { it },
        ) {
            AppliancesSelector(
                appliances = appliances,
                onSelected = onSelected,
                modifier = Modifier.padding(end = 16.dp),
            )
        }

        AnimatedContent(
            targetState = active,
            modifier = Modifier.padding(top = 8.dp, end = 16.dp),
            contentAlignment = Alignment.BottomEnd,
            transitionSpec = {
                slideInHorizontally { it } togetherWith
                        slideOutHorizontally { it }
            },
        ) { selectionActive ->
            ExtendedFloatingActionButton(
                onClick = {
                    if (active)
                        onShowPrices()
                    else
                        active = true
                },
                text = {
                    Text(stringResource(R.string.show_prices_button))
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.euro_symbol_48px),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                },
                expanded = selectionActive,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
            )
        }

    }

}

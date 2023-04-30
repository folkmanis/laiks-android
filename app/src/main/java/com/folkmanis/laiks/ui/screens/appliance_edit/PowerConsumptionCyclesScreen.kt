package com.folkmanis.laiks.ui.screens.appliance_edit

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.model.PowerApplianceCycle
import com.folkmanis.laiks.utilities.ext.toFormattedDecimals
import java.time.Duration

fun List<NullablePowerApplianceCycle>.updateAt(
    idx: Int,
    cycle: NullablePowerApplianceCycle
): List<NullablePowerApplianceCycle> = toMutableList().apply {
    this[idx] = cycle
}

fun List<NullablePowerApplianceCycle>.add(
    cycle: NullablePowerApplianceCycle
): List<NullablePowerApplianceCycle> =
    toMutableList().apply {
        add(cycle)
    }

fun List<NullablePowerApplianceCycle>.add(
    index: Int,
    cycle: NullablePowerApplianceCycle
): List<NullablePowerApplianceCycle> =
    toMutableList().apply {
        add(index, cycle)
    }

fun List<NullablePowerApplianceCycle>.removeAt(index: Int): List<NullablePowerApplianceCycle> =
    toMutableList().apply {
        removeAt(index)
    }

fun List<NullablePowerApplianceCycle>.toDuration(): Duration =
    sumOf { it.length ?: 0 }.let(Duration::ofMillis)

fun List<NullablePowerApplianceCycle>.kWh(): Double =
    sumOf { it.kWh }

@Composable
fun PowerConsumptionCyclesScreen(
    cycles: List<NullablePowerApplianceCycle>,
    onCyclesChange: (List<NullablePowerApplianceCycle>) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Column(modifier = modifier) {

        cycles.forEachIndexed { index, powerApplianceCycle ->
            PowerConsumptionCycleRow(
                order = index + 1,
                cycle = powerApplianceCycle,
                onCycleChange = {
                    onCyclesChange(cycles.updateAt(idx = index, cycle = it))
                },
                onAdd = {
                    onCyclesChange(
                        cycles.add(
                            index,
                            PowerApplianceCycle.zeroCycle.toNullablePowerApplianceCycle()
                        )
                    )
                },
                ondRemove = {
                    onCyclesChange(cycles.removeAt(index))
                },
                modifier = Modifier
                    .height(72.dp),
                enabled = enabled,
            )
        }

        Row(
            modifier = Modifier.height(56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(modifier = Modifier.width(32.dp))

            Box(modifier = Modifier.weight(1f)) {
                TotalDurationText(
                    duration = cycles.toDuration(),
                    modifier = Modifier
                        .padding(start = 16.dp)
                )
            }


            Box(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${cycles.kWh().toFormattedDecimals(digits = 2)} kWh",
                    modifier = Modifier
                        .padding(start = 16.dp)
                )
            }

            IconButton(
                onClick = {
                    onCyclesChange(
                        cycles.add(
                            PowerApplianceCycle.zeroCycle.toNullablePowerApplianceCycle()
                        )
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add)
                )
            }

            Spacer(modifier = Modifier.width(48.dp))

        }

    }

}

@Composable
fun TotalDurationText(
    duration: Duration,
    modifier: Modifier = Modifier
) {
    Text(
        text = with(duration) {
            "${toHours()} h, ${toMinutes() % 60} min"
        },
        modifier = modifier
    )
}
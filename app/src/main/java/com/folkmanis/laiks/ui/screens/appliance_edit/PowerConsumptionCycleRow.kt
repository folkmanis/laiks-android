package com.folkmanis.laiks.ui.screens.appliance_edit

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.model.PowerApplianceCycle

fun PowerApplianceCycle.isValid(): Boolean {
    return consumption >= 0 && length >= 0
}

fun Long.toMinutes(): Long =
    this / 60_000

fun Long.toMilliseconds(): Long =
    this * 60_000

@Composable
fun PowerConsumptionCycleRow(
    order: Int,
    cycle: PowerApplianceCycle,
    onCycleChange: (PowerApplianceCycle) -> Unit,
    onAdd: () -> Unit,
    ondRemove: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {

        Text(
            text = order.toString(),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .width(32.dp)
        )

        NumberInput(
            value = cycle.length.toMinutes(),
            onValueChange = {
                onCycleChange(
                    cycle.copy(length = it?.toMilliseconds() ?: 0)
                )
            },
            suffix = {
                Text(text = stringResource(id = R.string.units_minutes))
            },
            isError = cycle.length < 0,
            modifier = Modifier
                .weight(1f),
            enabled = enabled,
        )

        NumberInput(
            value = cycle.consumption,
            onValueChange = {
                onCycleChange(
                    cycle.copy(consumption = it ?: 0)
                )
            },
            suffix = {
                Text(text = stringResource(id = R.string.units_watts))
            },
            isError = cycle.consumption < 0,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f),
            enabled = enabled,
        )

        IconButton(
            onClick = onAdd,
            enabled = enabled,
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(id = R.string.add)
            )
        }

        IconButton(
            onClick = ondRemove,
            enabled = enabled,
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = stringResource(id = R.string.remove)
            )
        }

    }
}

@Preview
@Composable
fun PowerConsumptionCycleRowPreview() {
    var cycle by remember {
        mutableStateOf(PowerApplianceCycle())
    }
    MaterialTheme {
        PowerConsumptionCycleRow(
            order = 10,
            cycle = cycle,
            onCycleChange = { cycle = it },
            onAdd = {},
            ondRemove = {}
        )
    }
}
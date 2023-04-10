package com.folkmanis.laiks.ui.screens.appliance_edit

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.folkmanis.laiks.model.PowerApplianceCycle

fun List<PowerApplianceCycle>.updateAt(
    idx: Int,
    cycle: PowerApplianceCycle
): List<PowerApplianceCycle> = this.toMutableList().apply {
    this[idx] = cycle
}

@Composable
fun PowerConsumptionCycles(
    cycles: List<PowerApplianceCycle>,
    onCyclesChange: (List<PowerApplianceCycle>) -> Unit,
    modifier: Modifier = Modifier,
) {
    cycles.forEachIndexed { index, powerApplianceCycle ->
        PowerConsumptionCycleRow(
            order = index,
            cycle = powerApplianceCycle,
            onCycleChange = { cycles.updateAt(idx = index, cycle = it) },
            onAdd = { /*TODO*/ },
            ondRemove = { /*TODO*/ })
    }

}

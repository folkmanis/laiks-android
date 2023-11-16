package com.folkmanis.laiks.ui.screens.user_settings.appliances

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.data.fake.FakeAppliancesService
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.utilities.composables.ListDivider
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable


@Composable
internal fun AppliancesScreen(
    appliances: List<PowerAppliance>,
    modifier: Modifier = Modifier,
    onEdit: (idx: Int) -> Unit,
    onDelete: (idx: Int) -> Unit,
    onAdd: () -> Unit,
    onReorder: (from: Int, to: Int) -> Unit,
    onReorderEnd: () -> Unit,
    onSelectAppliance: (idx: Int, name: String) -> Unit,
    busy: Boolean,
) {

    val reorderableState = rememberReorderableLazyListState(
        onMove = { from, to ->
            onReorder(from.index, to.index)
        },
        onDragEnd = { _, _ -> onReorderEnd() }
    )

    Box(modifier = modifier.fillMaxSize()) {

        LazyColumn(
            state = reorderableState.listState,
            modifier = Modifier
                .reorderable(reorderableState)
                .fillMaxHeight()
        ) {
            itemsIndexed(
                items = appliances,
                key = { _, appliance -> appliance.name }
            )
            { idx, appliance ->

                ReorderableItem(
                    reorderableState = reorderableState,
                    key = appliance.name
                ) { isDragging ->
                    val elevation = animateDpAsState(
                        targetValue = if (isDragging) 16.dp else 0.dp,
                        label = "DragShadow",
                    )

                    Column(
                        modifier = Modifier
                            .shadow(elevation.value)
                    ) {

                        ApplianceRow(
                            name = appliance.name,
                            color = appliance.color,
                            onEdit = { onEdit(idx) },
                            onDelete = { onDelete(idx) },
                            busy = busy,
                            reorderableState = reorderableState,
                            onSchedule = { onSelectAppliance(idx, appliance.name) }
                        )

                        ListDivider()

                    }

                }

            }
        }

        FloatingActionButton(
            onClick = onAdd,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(id = R.string.add)
            )
        }


    }

}

@Preview
@Composable
fun AppliancesScreenPreview() {
    MaterialTheme {
        AppliancesScreen(
            onDelete = {},
            onEdit = {},
            onSelectAppliance = { _, _ -> },
            onAdd = {},
            appliances = FakeAppliancesService.testAppliances,
            busy = false,
            onReorder = { _, _ -> },
            onReorderEnd = {},
        )
    }
}
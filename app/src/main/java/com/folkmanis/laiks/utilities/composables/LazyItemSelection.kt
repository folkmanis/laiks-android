package com.folkmanis.laiks.utilities.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.folkmanis.laiks.data.fake.FakeMarketZonesService
import com.folkmanis.laiks.model.DataWithIdAndDescription

@Composable
fun LazyItemSelection(
    data: List<DataWithIdAndDescription>,
    modifier: Modifier = Modifier,
    onItemSelected: (String) -> Unit,
    selectedId: String? = null,
) {
    LazyColumn(modifier = modifier) {
        items(data, key = { it.id }) { zone ->
            ListItem(
                headlineContent = {
                    Text(
                        text = "${zone.id}, ${zone.description}",
                        modifier = Modifier
                            .clickable { onItemSelected(zone.id) }
                    )
                },
                leadingContent = {
                    RadioButton(
                        selected = zone.id == selectedId,
                        onClick = { onItemSelected(zone.id) },
                    )
                }
            )
        }
    }

}

@Preview
@Composable
fun LazyItemSelectionPreview() {
    val data = FakeMarketZonesService.zonesList
    MaterialTheme {
        LazyItemSelection(
            data = data,
            onItemSelected = {}
        )
    }
}
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
    dataList: List<DataWithIdAndDescription>,
    modifier: Modifier = Modifier,
    onItemSelected: (String) -> Unit,
    selectedId: String? = null,
) {
    LazyColumn(modifier = modifier) {
        items(dataList, key = { it.id }) { data ->
            ListItem(
                headlineContent = {
                    Text(
                        text = "${data.id}, ${data.description}",
                    )
                },
                leadingContent = {
                    RadioButton(
                        selected = data.id == selectedId,
                        onClick = null,
                    )
                },
                modifier = Modifier
                    .clickable { onItemSelected(data.id) }
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
            dataList = data,
            onItemSelected = {}
        )
    }
}
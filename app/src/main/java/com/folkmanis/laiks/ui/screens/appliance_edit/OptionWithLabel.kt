package com.folkmanis.laiks.ui.screens.appliance_edit

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R

@Composable
fun OptionWithLabel(
    label: String,
    modifier: Modifier = Modifier,
    projection: @Composable ColumnScope.() -> Unit
) {

    Column(
        modifier = modifier.
                padding(start = 8.dp)
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
        )

        Spacer(modifier = Modifier.height(16.dp))

        projection()
    }

}
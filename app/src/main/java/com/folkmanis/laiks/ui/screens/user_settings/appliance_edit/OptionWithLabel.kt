package com.folkmanis.laiks.ui.screens.user_settings.appliance_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OptionWithLabel(
    label: String,
    modifier: Modifier = Modifier,
    projection: @Composable ColumnScope.() -> Unit
) {

    Column(
        modifier = modifier
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            modifier=Modifier.padding(start = 8.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        projection()
    }

}
package com.folkmanis.laiks.utilities.composables

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ListDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        thickness = 2.dp,
        modifier = modifier,
    )
}
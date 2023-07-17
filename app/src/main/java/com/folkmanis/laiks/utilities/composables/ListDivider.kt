package com.folkmanis.laiks.utilities.composables

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ListDivider(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier.height(2.dp)
    )
}
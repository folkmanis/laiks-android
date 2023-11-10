package com.folkmanis.laiks.utilities.composables

import androidx.annotation.StringRes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun TextOrEmpty(
    @StringRes id: Int?,
    modifier: Modifier = Modifier
) {
    Text(
        text = id?.let { stringResource(id = it) } ?: "",
        modifier = modifier
    )
}
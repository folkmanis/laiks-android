package com.folkmanis.laiks.utilities.modifiers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun Modifier.settingsRow(): Modifier =
    this
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .height(80.dp)

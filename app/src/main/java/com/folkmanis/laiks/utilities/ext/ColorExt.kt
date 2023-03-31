package com.folkmanis.laiks.utilities.ext

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

fun Color.isDark() =
    luminance() < 0.5
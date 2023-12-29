package com.folkmanis.laiks.utilities.ext

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

fun Color.isDark() =
    luminance() < 0.5

fun Color.contrasting(): Color =
    if (isDark()) Color.White else Color.Black

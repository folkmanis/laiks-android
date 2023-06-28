package com.folkmanis.laiks.ui.screens.laiks

import com.folkmanis.laiks.model.LaiksUser
import com.folkmanis.laiks.model.Permissions

data class LaiksAppState(
    val user: LaiksUser? = null,
    val title: String = "",
)
package com.folkmanis.laiks.ui.screens.laiks

import com.folkmanis.laiks.model.LaiksUser
import com.google.firebase.auth.FirebaseUser

data class LaiksAppState(
    val laiksUser: LaiksUser? = null,
    val title: String = "",
    val user: FirebaseUser? = null,
    val npBlocked: Boolean = false,
)
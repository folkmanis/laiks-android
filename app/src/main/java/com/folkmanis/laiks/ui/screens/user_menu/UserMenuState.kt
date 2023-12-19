package com.folkmanis.laiks.ui.screens.user_menu

import android.net.Uri

data class UserMenuState(
    val expanded: Boolean = false,
    val isVatEnabled: Boolean = false,
    val isAnonymous: Boolean = true,
    val npBlocked: Boolean = true,
    val photoUrl: Uri? = null,
    val name: String? = null,
)
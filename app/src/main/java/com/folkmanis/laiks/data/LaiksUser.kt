package com.folkmanis.laiks.data

data class LaiksUser(
    val email: String = "",
    val isAdmin: Boolean = false,
    val name: String = "",
    val npAllowed: Boolean = false,
    val verified: Boolean = false,
)

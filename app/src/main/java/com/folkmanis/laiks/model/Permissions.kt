package com.folkmanis.laiks.model

data class Permissions(
    val npUser: Boolean = false,
    val admin: Boolean = false,
    val npBlocked: Boolean = false,
)

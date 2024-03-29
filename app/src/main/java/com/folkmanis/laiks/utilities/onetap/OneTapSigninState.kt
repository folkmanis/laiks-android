package com.folkmanis.laiks.utilities.onetap

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class OneTapSigninState {
    var opened by mutableStateOf(false)
        private set

    fun open() {
        opened = true
    }

    internal fun close() {
        opened = false
    }

}
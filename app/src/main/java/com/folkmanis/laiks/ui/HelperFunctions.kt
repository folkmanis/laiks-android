package com.folkmanis.laiks.ui

internal fun Int.toSignedString(): String {
    return if(this > 0) {
        "+${toString()}"
    } else {
        toString()
    }
}

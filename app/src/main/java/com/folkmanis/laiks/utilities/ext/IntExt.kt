package com.folkmanis.laiks.utilities.ext

fun Int.toSignedString(): String {
    return if (this > 0) {
        "+${toString()}"
    } else {
        toString()
    }
}

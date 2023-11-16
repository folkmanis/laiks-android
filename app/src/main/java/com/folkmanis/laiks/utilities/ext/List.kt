package com.folkmanis.laiks.utilities.ext

fun <T> MutableList<T>.swap(from: Int, to: Int): MutableList<T> {
    this[to] = this[from].also { this[from] = this[to] }
    return this
}
package com.folkmanis.laiks.utilities

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.time.Instant
import java.time.LocalDateTime

fun minuteTicks() = flow<LocalDateTime> {
    while (true) {
        val now = LocalDateTime.now()
        emit(now)
        val delayToNext = delayToNextMinute(LocalDateTime.now())
        delay(delayToNext)
    }
}

fun hourTicks() = flow<LocalDateTime> {
    while (true) {
        val now = LocalDateTime.now()
        emit(now)
        val delayToNext = delayToNextMinute(LocalDateTime.now())
        delay(delayToNext)
    }
}

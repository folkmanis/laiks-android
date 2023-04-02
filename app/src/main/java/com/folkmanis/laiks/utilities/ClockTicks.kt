package com.folkmanis.laiks.utilities

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

fun minuteTicks() = flow<LocalDateTime> {
    while (true) {
        val now = LocalDateTime.now()
        emit(now.truncatedTo(ChronoUnit.MINUTES))
        val delayToNext = delayToNextMinute(LocalDateTime.now())
        delay(delayToNext)
    }
}

fun hourTicks() = flow<LocalDateTime> {
    while (true) {
        val now = LocalDateTime.now()
        emit(now.truncatedTo(ChronoUnit.HOURS))
        val delayToNext = delayToNextHour(LocalDateTime.now())
        delay(delayToNext)
    }
}

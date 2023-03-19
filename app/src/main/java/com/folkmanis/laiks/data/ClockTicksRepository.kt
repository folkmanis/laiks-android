package com.folkmanis.laiks.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.time.Duration
import java.time.LocalDateTime

class ClockTicksRepository {

    fun clockTicksFlow() = flow<LocalDateTime> {
        while (true) {
            val now = LocalDateTime.now()
            emit(now)
            delay(
                delayToNextSecond(now)
            )
        }
    }

     fun delayToNextSecond(fromDateTime: LocalDateTime): Long {
        val nextSecond = fromDateTime
            .plusSeconds(1)
            .withNano(0)
        return Duration
            .between(fromDateTime, nextSecond)
            .toMillis()
    }

    fun delayToNextMinute(fromDateTime: LocalDateTime): Long {
        val nextMinute = fromDateTime
            .plusMinutes(1)
            .withSecond(0)
            .withNano(0)
        return Duration
            .between(fromDateTime, nextMinute)
            .toMillis()
    }

}
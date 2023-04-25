package com.folkmanis.laiks.data.domain

import com.folkmanis.laiks.REFRESH_AT_HOURS
import com.folkmanis.laiks.REFRESH_AT_MINUTES
import com.folkmanis.laiks.REFRESH_AT_TZ
import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.utilities.millisecondsToNextUpdate
import java.time.Instant
import javax.inject.Inject

class DelayToNextNpUpdateUseCase @Inject constructor(
    private val pricesRepository: PricesService,
) {

    suspend operator fun invoke(): Long {
        val lastUpdateTime = pricesRepository.lastUpdate()
        return millisecondsToNextUpdate(
            Instant.now(),
            lastUpdateTime,
            REFRESH_AT_HOURS, REFRESH_AT_MINUTES, REFRESH_AT_TZ
        )
    }
}
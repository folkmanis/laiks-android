package com.folkmanis.laiks.data.domain

import com.folkmanis.laiks.VAT
import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.data.UserPreferencesRepository
import com.folkmanis.laiks.ui.screens.prices.PricesStatistics
import com.folkmanis.laiks.utilities.ext.withVat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StatisticsUseCase @Inject constructor(
    private val pricesService: PricesService,
    userPreferencesRepository: UserPreferencesRepository,
) {

    private val vatAmount = userPreferencesRepository.includeVat
        .map { if (it) VAT else 1.0 }

    operator fun invoke(): Flow<PricesStatistics> = pricesService
        .npPricesDocumentFlow()
        .filterNotNull()
        .combine(vatAmount) { doc, vat ->
            if (doc.average != null && doc.stDev != null) {
                PricesStatistics(
                    average = doc.average.withVat(vat),
                    stDev = doc.stDev.withVat(vat)
                )
            } else {
                null
            }
        }
        .filterNotNull()
}

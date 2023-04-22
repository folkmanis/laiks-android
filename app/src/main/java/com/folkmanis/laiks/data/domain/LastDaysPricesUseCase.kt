package com.folkmanis.laiks.data.domain

import com.folkmanis.laiks.VAT
import com.folkmanis.laiks.data.PricesService
import com.folkmanis.laiks.data.UserPreferencesRepository
import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.utilities.ext.withVat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LastDaysPricesUseCase @Inject constructor(
    private val pricesService: PricesService,
    userPreferencesRepository: UserPreferencesRepository,
) {

    private val vatAmount = userPreferencesRepository.includeVat
        .map { if (it) VAT else 1.0 }

    operator fun invoke(includeDays: Long): Flow<List<NpPrice>> = pricesService
        .lastDaysPricesFlow(includeDays)
        .filter(List<NpPrice>::isNotEmpty)
        .combine(vatAmount) { prices, vat ->
            prices.addVat(vat)
        }

    companion object {
        private fun List<NpPrice>.addVat(amount: Double): List<NpPrice> =
            map { it.copy(value = it.value.withVat(amount)) }
    }


}
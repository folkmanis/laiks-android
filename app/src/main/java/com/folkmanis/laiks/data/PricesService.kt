package com.folkmanis.laiks.data

import com.folkmanis.laiks.model.NpPrice
import kotlinx.coroutines.flow.Flow

interface PricesService {
    val allNpPrices: Flow<List<NpPrice>>
}
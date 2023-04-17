package com.folkmanis.laiks.data.implementations

import com.folkmanis.laiks.data.NpRepository
import com.folkmanis.laiks.data.network.NpApiService
import com.folkmanis.laiks.model.NpPrice
import javax.inject.Inject

class NetworkNpRepository @Inject constructor(
    private val npApiService: NpApiService
): NpRepository {
    override suspend fun getNpData(): List<NpPrice> =
//    override suspend fun getNpData(): String =
        npApiService.getNpData()
}
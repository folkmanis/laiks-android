package com.folkmanis.laiks.data.implementations

import com.folkmanis.laiks.data.NpRepository
import com.folkmanis.laiks.data.network.NpApiService
import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.np_data.NpServerData
import javax.inject.Inject

class NetworkNpRepository @Inject constructor(
    private val npApiService: NpApiService
): NpRepository {
    override suspend fun getNpData(): NpServerData =
        npApiService.getNpData()
}
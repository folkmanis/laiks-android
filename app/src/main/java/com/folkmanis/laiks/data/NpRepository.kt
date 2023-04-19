package com.folkmanis.laiks.data

import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.np_data.NpServerData

interface NpRepository {
    suspend fun getNpData(): NpServerData
}
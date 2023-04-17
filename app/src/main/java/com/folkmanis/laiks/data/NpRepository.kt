package com.folkmanis.laiks.data

import com.folkmanis.laiks.model.NpPrice

interface NpRepository {
//    suspend fun getNpData(): String
    suspend fun getNpData(): List<NpPrice>
}
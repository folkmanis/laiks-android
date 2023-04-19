package com.folkmanis.laiks.data.network

import com.folkmanis.laiks.DAY_AHEAD_LV
import com.folkmanis.laiks.model.np_data.NpServerData
import retrofit2.http.GET

interface NpApiService {

    @GET(DAY_AHEAD_LV)
    suspend fun getNpData(): NpServerData
}
package com.folkmanis.laiks.data.network

import com.folkmanis.laiks.model.NpPrice
import com.folkmanis.laiks.model.np_data.NpServerData
import retrofit2.http.GET

interface NpApiService {

    @GET("59?currency=,EUR,EUR,EUR")
    suspend fun getNpData(): NpServerData
}
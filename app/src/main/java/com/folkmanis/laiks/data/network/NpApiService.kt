package com.folkmanis.laiks.data.network

import com.folkmanis.laiks.model.NpPrice
import retrofit2.http.GET

interface NpApiService {

    @GET("59?currency=,EUR,EUR,EUR")
//    suspend fun getNpData(): String
    suspend fun getNpData(): List<NpPrice>
}
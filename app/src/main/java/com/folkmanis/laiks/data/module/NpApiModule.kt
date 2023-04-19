package com.folkmanis.laiks.data.module

import com.folkmanis.laiks.data.NpRepository
import com.folkmanis.laiks.data.implementations.NetworkNpRepository
import com.folkmanis.laiks.data.network.NpApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

private const val BASE_URL = "https://www.nordpoolgroup.com/api/marketdata/page/"

@OptIn(ExperimentalSerializationApi::class)
@InstallIn(SingletonComponent::class)
@Module
object NpApiModule {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit
        .Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    @Singleton
    @Provides
    fun provideNpApiService(retrofit: Retrofit): NpApiService =
        retrofit.create(NpApiService::class.java)

    @Singleton
    @Provides
    fun providesRepository(npApiService: NpApiService) =
        NetworkNpRepository(npApiService)

}
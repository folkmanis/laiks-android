package com.folkmanis.laiks.data

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val savedTimeOffset: Flow<Int>
    val includeVat: Flow<Boolean>
    suspend fun saveTimeOffset(offset: Int)
    suspend fun setIncludeVat(include: Boolean)
}

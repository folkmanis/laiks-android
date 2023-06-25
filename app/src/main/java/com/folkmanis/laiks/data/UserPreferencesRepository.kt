package com.folkmanis.laiks.data

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val savedTimeOffset: Flow<Int>
    suspend fun saveTimeOffset(offset: Int)
}

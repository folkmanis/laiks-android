package com.folkmanis.laiks.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface UserPreferencesRepository {
    val savedTimeOffset: Flow<Int>
    suspend fun saveTimeOffset(offset: Int)
}

object  FakeUserPreferencesRepository : UserPreferencesRepository {
    override val savedTimeOffset: MutableStateFlow<Int> = MutableStateFlow(1)

    override suspend fun saveTimeOffset(offset: Int) {
        savedTimeOffset.emit(offset)
    }
}
package com.folkmanis.laiks.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface UserPreferencesRepository {
    val savedTimeOffset: Flow<Int>
    val includeVat: Flow<Boolean>
    suspend fun saveTimeOffset(offset: Int)
    suspend fun setIncludeVat(include: Boolean)
}

object  FakeUserPreferencesRepository : UserPreferencesRepository {
    override val savedTimeOffset: MutableStateFlow<Int> = MutableStateFlow(1)

    override suspend fun saveTimeOffset(offset: Int) {
        savedTimeOffset.emit(offset)
    }

    override val includeVat: MutableStateFlow<Boolean> = MutableStateFlow(true)

    override suspend fun setIncludeVat(include: Boolean) {
        includeVat.emit(include)
    }
}
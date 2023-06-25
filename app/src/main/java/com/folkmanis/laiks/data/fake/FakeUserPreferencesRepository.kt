package com.folkmanis.laiks.data.fake

import com.folkmanis.laiks.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserPreferencesRepository : UserPreferencesRepository {

    override val savedTimeOffset: MutableStateFlow<Int> = MutableStateFlow(1)

    override suspend fun saveTimeOffset(offset: Int) {
        savedTimeOffset.emit(offset)
    }

}
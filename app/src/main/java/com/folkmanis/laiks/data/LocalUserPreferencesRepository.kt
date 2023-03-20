package com.folkmanis.laiks.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class LocalUserPreferencesRepository(
    private val datastore: DataStore<Preferences>
) : UserPreferencesRepository {

    private companion object {
        val TIME_OFFSET_KEY = intPreferencesKey("time_offset")
        const val TAG = "User Preferences Datastore"
    }

    override val savedTimeOffset: Flow<Int> = datastore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Preferences read error", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[TIME_OFFSET_KEY] ?: 0
        }

    override suspend fun saveTimeOffset(offset: Int) {
        datastore.edit { preferences ->
            preferences[TIME_OFFSET_KEY] = offset
        }
    }

}
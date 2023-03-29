package com.folkmanis.laiks.data.implementations

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.folkmanis.laiks.data.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class LocalUserPreferencesRepository @Inject constructor (
    private val datastore: DataStore<Preferences>
) : UserPreferencesRepository {

    private companion object {
        val TIME_OFFSET_KEY = intPreferencesKey("time_offset")
        val INCLUDE_VAT_KEY = booleanPreferencesKey("include_vat")
        const val TAG = "User Preferences Datastore"
    }

    private val allPreferences: Flow<Preferences>
        get() = datastore.data
            .catch {
                if (it is IOException) {
                    Log.e(TAG, "Preferences read error", it)
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }

    override val savedTimeOffset: Flow<Int>
        get() = allPreferences
            .map { preferences ->
                preferences[TIME_OFFSET_KEY] ?: 0
            }

    override val includeVat: Flow<Boolean>
        get() = allPreferences
            .map { preferences ->
                preferences[INCLUDE_VAT_KEY] ?: true
            }

    override suspend fun saveTimeOffset(offset: Int) {
        datastore.edit { preferences ->
            preferences[TIME_OFFSET_KEY] = offset
        }
    }

    override suspend fun setIncludeVat(include: Boolean) {
        datastore.edit { preferences ->
            preferences[INCLUDE_VAT_KEY] = include
        }
    }


}
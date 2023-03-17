package com.folkmanis.laiks

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.folkmanis.laiks.data.UserPreferencesRepository

private const val LAIKS_PREFERENCE_NAME = "laiks_preferences"
private val Context.dataStore by preferencesDataStore(
    name = LAIKS_PREFERENCE_NAME
)

class LaiksApplication : Application() {
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        userPreferencesRepository =
            UserPreferencesRepository(dataStore)
    }
}
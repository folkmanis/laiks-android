package com.folkmanis.laiks

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.AccountServiceFirebase
import com.folkmanis.laiks.data.LocalUserPreferencesRepository
import com.folkmanis.laiks.data.UserPreferencesRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val LAIKS_PREFERENCE_NAME = "laiks_preferences"
private val Context.dataStore by preferencesDataStore(
    name = LAIKS_PREFERENCE_NAME
)

class LaiksApplication : Application() {
    lateinit var userPreferencesRepository: UserPreferencesRepository
    lateinit var accountService: AccountService
    override fun onCreate() {
        super.onCreate()
        userPreferencesRepository =
            LocalUserPreferencesRepository(dataStore)

        accountService =
            AccountServiceFirebase(Firebase.auth)
    }
}
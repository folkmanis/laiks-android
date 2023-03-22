package com.folkmanis.laiks

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.folkmanis.laiks.data.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val LAIKS_PREFERENCE_NAME = "laiks_preferences"
private val Context.dataStore by preferencesDataStore(
    name = LAIKS_PREFERENCE_NAME
)

class LaiksApplication : Application() {
    lateinit var userPreferencesRepository: UserPreferencesRepository
    lateinit var accountService: AccountService
    lateinit var pricesService: PricesService
    override fun onCreate() {
        super.onCreate()

        val firestore = Firebase.firestore
        userPreferencesRepository =
            LocalUserPreferencesRepository(dataStore)

        accountService =
            AccountServiceFirebase(
                Firebase.auth,
                firestore
            )

        pricesService =
            PricesServiceFirebase(
                firestore
            )

    }
}
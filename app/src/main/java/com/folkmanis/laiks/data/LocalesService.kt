package com.folkmanis.laiks.data

import com.folkmanis.laiks.model.Locale
import kotlinx.coroutines.flow.Flow

interface LocalesService {

   val localesFlow: Flow<List<Locale>>

    suspend fun getLocales(): List<Locale>

    suspend fun getLocale(id: String): Locale?

}
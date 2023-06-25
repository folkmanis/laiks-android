package com.folkmanis.laiks.data

import com.folkmanis.laiks.model.Locale

interface LocalesService {

    suspend fun getLocales(): List<Locale>

    suspend fun getLocale(id: String): Locale?

}
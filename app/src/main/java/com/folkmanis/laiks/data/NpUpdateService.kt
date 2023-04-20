package com.folkmanis.laiks.data

interface NpUpdateService {

  suspend fun updateNpPrices(): Int

}
package com.folkmanis.laiks.model

import com.google.firebase.Timestamp

data class NpPricesDocument(
   val lastUpdate: Timestamp = Timestamp.now()
)

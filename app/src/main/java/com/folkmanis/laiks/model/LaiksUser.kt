package com.folkmanis.laiks.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class LaiksUser(

    @DocumentId val id: String = "",
    val email: String = "",

    // or field will be renamed to 'admin'
//    @get:PropertyName("isAdmin")
    @field:JvmField
    val isAdmin: Boolean = false,

    val name: String = "",
    val npAllowed: Boolean = false,
    val verified: Boolean = false,
)

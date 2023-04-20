package com.folkmanis.laiks.model

import com.google.firebase.firestore.DocumentId

data class LaiksUser(

    @DocumentId val id: String = "",
    val email: String = "",

    // or field will be renamed to 'admin'
    @field:JvmField
    val isAdmin: Boolean = false,

    val name: String = "",
    val npAllowed: Boolean = false,
    val npUploadAllowed: Boolean = false,
    val verified: Boolean = false,
)

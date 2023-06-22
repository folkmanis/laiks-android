package com.folkmanis.laiks.data

import com.folkmanis.laiks.model.LaiksUser
import com.folkmanis.laiks.model.PowerAppliance
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AccountService {

    val firebaseUserFlow: Flow<FirebaseUser?>

    val authUser: FirebaseUser?

    suspend fun signWithEmailAndPassword(email: String, password: String)

}
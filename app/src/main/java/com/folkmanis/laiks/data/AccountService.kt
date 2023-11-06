package com.folkmanis.laiks.data

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AccountService {

    val firebaseUserFlow: Flow<FirebaseUser?>

    val authUser: FirebaseUser?

   suspend fun loginWithEmail(email: String, password: String):AuthResult

}
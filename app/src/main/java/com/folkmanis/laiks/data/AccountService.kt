package com.folkmanis.laiks.data

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AccountService {

    val firebaseUserFlow: Flow<FirebaseUser?>

    val authUser: FirebaseUser?

    suspend fun loginWithEmail(email: String, password: String): AuthResult

    suspend fun resetPassword(email: String)

    suspend fun createUserWithEmail(email: String, password: String, name: String)

    suspend fun sendEmailVerification()

    suspend fun deleteAccount()

    suspend fun signOut()

}
package com.folkmanis.laiks.data

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AccountService {

    val firebaseUserFlow: Flow<FirebaseUser?>

    val authUser: FirebaseUser?

    suspend fun loginWithEmail(email: String, password: String): AuthResult

    suspend fun loginWithGoogle(idToken: String): AuthResult

    suspend fun loginWithCredential(credential: AuthCredential): AuthResult

    suspend fun linkWithCredential(credential: AuthCredential): AuthResult

    suspend fun resetPassword(email: String): Void

    suspend fun createUserWithEmail(email: String, password: String, name: String)

    suspend fun sendEmailVerification()

    suspend fun deleteAccount()

    fun signOut()

}
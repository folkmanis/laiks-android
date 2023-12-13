package com.folkmanis.laiks.data

import android.net.Uri
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AccountService {

    val firebaseUserFlow: Flow<FirebaseUser?>

    val authUser: FirebaseUser?

    val isRegistered: Flow<Boolean>

    suspend fun loginWithCredential(credential: AuthCredential): AuthResult

    suspend fun linkWithCredential(
        credential: AuthCredential,
        displayName: String?,
        uri: Uri?,
    )

    suspend fun resetPassword(email: String): Void

    suspend fun createUserWithEmail(email: String, password: String, name: String)

    suspend fun sendEmailVerification()

    suspend fun createAnonymous(): AuthResult

    suspend fun deleteAccount()

    fun signOut()

}
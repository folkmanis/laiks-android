package com.folkmanis.laiks.data.implementations

import com.folkmanis.laiks.data.AccountService
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Locale
import javax.inject.Inject

class AccountServiceFirebase @Inject constructor(
    private val auth: FirebaseAuth,
) : AccountService {

    override val firebaseUserFlow: Flow<FirebaseUser?>
        get() = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener {
                this.trySend(it.currentUser)
            }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    override val authUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun loginWithEmail(email: String, password: String): AuthResult {
        return auth
            .signInWithEmailAndPassword(email, password)
            .await()
    }

    override suspend fun resetPassword(email: String) {
        setLanguage()
        auth
            .sendPasswordResetEmail(email)
            .await()
    }

    override suspend fun createUserWithEmail(email: String, password: String, name: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
        authUser?.also { user ->
            val profileUpdate = userProfileChangeRequest {
                displayName = name
            }
            user.updateProfile(profileUpdate).await()
            setLanguage()
            user.sendEmailVerification().await()
        }
    }

    override suspend fun deleteAccount() {
        authUser?.run {
            delete().await()
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    private fun setLanguage() {
        val locale = Locale.getDefault().language
        auth.setLanguageCode(locale)
    }

    companion object {
        @Suppress("unused")
        private const val TAG = "Account Service"
    }

}
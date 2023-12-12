package com.folkmanis.laiks.data.implementations

import com.firebase.ui.auth.FirebaseAuthAnonymousUpgradeException
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.utilities.NotLoggedInException
import com.folkmanis.laiks.utilities.UserNotAnonymousException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
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

    override val isRegistered: Flow<Boolean>
        get() = firebaseUserFlow
            .map { user ->
                user?.isAnonymous ?: false
            }

    override suspend fun loginWithCredential(credential: AuthCredential): AuthResult {
        return auth.signInWithCredential(credential).await()
    }

    override suspend fun linkWithCredential(credential: AuthCredential) {
        val currentUser = authUser ?: throw NotLoggedInException()
        if (!currentUser.isAnonymous) throw UserNotAnonymousException()
        currentUser.linkWithCredential(credential).await()
        currentUser.reload().await()
    }

    override suspend fun resetPassword(email: String): Void {
        setLanguage()
        return auth
            .sendPasswordResetEmail(email)
            .await()
    }

    override suspend fun createUserWithEmail(email: String, password: String, name: String) {

        val task =
            authUser?.linkWithCredential(EmailAuthProvider.getCredential(email, password))
                ?: auth.createUserWithEmailAndPassword(email, password)

        task.await().user?.also { user ->
            val profileUpdate = userProfileChangeRequest {
                displayName = name
            }
            user.updateProfile(profileUpdate).await()
        }
    }

    override suspend fun createAnonymous(): AuthResult {
        return auth
            .signInAnonymously()
            .await()
    }

    override suspend fun sendEmailVerification() {
        authUser?.also { user ->
            setLanguage()
            user.sendEmailVerification().await()
        }
    }

    override suspend fun deleteAccount() {
        authUser?.run {
            delete().await()
        }
    }

    override fun signOut() {
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
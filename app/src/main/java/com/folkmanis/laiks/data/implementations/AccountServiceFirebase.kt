package com.folkmanis.laiks.data.implementations

import com.folkmanis.laiks.data.AccountService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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


    override suspend fun signWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
    }

    companion object {
        private const val TAG = "Account Service"
    }

}
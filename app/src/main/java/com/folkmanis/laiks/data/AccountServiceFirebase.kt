package com.folkmanis.laiks.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class AccountServiceFirebase(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : AccountService {

    override val currentUser: Flow<FirebaseUser?>
        get() = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener {
                this.trySend(it.currentUser)
            }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    override suspend fun getLaiksUser(uId: String): Flow<LaiksUser?> =
        firestore.collection(USER_COLLECTION)
            .document(uId)
            .snapshots()
            .map { document ->
                Log.d(TAG, "Laiks user ${document.data}")
                document.toObject<LaiksUser>()
            }

    override suspend fun signWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email,password)
    }
    override suspend fun signOut() {
        auth.signOut()
    }

    companion object {
        private const val TAG = "Account Service"
        private const val USER_COLLECTION = "users"
    }

}
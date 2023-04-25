package com.folkmanis.laiks.data.implementations

import android.util.Log
import com.folkmanis.laiks.USER_COLLECTION
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.model.LaiksUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class AccountServiceFirebase @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
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

    override val laiksUserFlow: Flow<LaiksUser?>
        get() = firebaseUserFlow
            .flatMapLatest { user ->
                if (user == null) {
                    flowOf(null)
                } else {
                    laiksUserFlow(user.uid)
                }
            }


    override fun laiksUsersFlow(): Flow<List<LaiksUser>> =
        firestore.collection(USER_COLLECTION)
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects()
            }

    override fun laiksUserFlow(uId: String): Flow<LaiksUser?> =
        firestore.collection(USER_COLLECTION)
            .document(uId)
            .snapshots()
            .map { document ->
                Log.d(TAG, "Laiks user ${document.data}")
                document.toObject<LaiksUser>()
            }

    override suspend fun laiksUser(uId: String): LaiksUser? =
        firestore.collection(USER_COLLECTION)
            .document(uId)
            .get()
            .await()
            .toObject()

    override suspend fun userExists(id: String): Boolean =
        firestore.collection(USER_COLLECTION)
            .document(id)
            .get()
            .await()
            .exists()

    override suspend fun createLaiksUser(user: FirebaseUser) {
        val laiksUser = LaiksUser(
            id = user.uid,
            name = user.displayName ?: "",
            email = user.email ?: "",
            isAdmin = false,
            npAllowed = false,
            verified = false
        )
        Log.d(TAG, "new user: $user")
        firestore.collection(USER_COLLECTION)
            .document(user.uid)
            .set(laiksUser)
            .await()
    }

    override suspend fun updateLaiksUser(user: LaiksUser) {
        firestore.collection(USER_COLLECTION)
            .document(user.id)
            .set(user)
            .await()
    }

    override suspend fun signWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
    }

    companion object {
        private const val TAG = "Account Service"
    }

}
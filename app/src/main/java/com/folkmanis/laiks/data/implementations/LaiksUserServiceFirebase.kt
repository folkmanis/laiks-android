package com.folkmanis.laiks.data.implementations

import android.util.Log
import com.folkmanis.laiks.USER_APPLIANCES_COLLECTION
import com.folkmanis.laiks.USER_COLLECTION
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.model.LaiksUser
import com.folkmanis.laiks.model.PowerAppliance
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LaiksUserServiceFirebase @Inject constructor(
    firestore: FirebaseFirestore,
) : LaiksUserService {

    private val collection = firestore.collection(USER_COLLECTION)

    override fun laiksUsersFlow(): Flow<List<LaiksUser>> =
        collection.snapshots()
            .map { snapshot ->
                snapshot.toObjects()
            }

    override fun laiksUserFlow(uId: String): Flow<LaiksUser?> =
        collection.document(uId)
            .snapshots()
            .map { document ->
                Log.d(TAG, "Laiks user ${document.data}")
                document.toObject<LaiksUser>()
            }

    override suspend fun laiksUser(uId: String): LaiksUser? =
        collection.document(uId)
            .get()
            .await()
            .toObject()

    override suspend fun userAppliances(uId: String): List<PowerAppliance> =
        collection.document(uId)
            .collection(USER_APPLIANCES_COLLECTION)
            .get()
            .await()
            .toObjects()


    override suspend fun userExists(id: String): Boolean =
        collection.document(id)
            .get()
            .await()
            .exists()

    override suspend fun createLaiksUser(user: FirebaseUser) {
        val laiksUser = LaiksUser(
            id = user.uid,
            name = user.displayName ?: "",
            email = user.email ?: "",
            verified = false
        )
        Log.d(TAG, "new user: $user")
        collection
            .document(user.uid)
            .set(laiksUser)
            .await()
    }

    override suspend fun updateLaiksUser(user: LaiksUser) {
        collection
            .document(user.id)
            .set(user)
            .await()
    }

    companion object {
        private const val TAG = "LaiksUserService"
    }
}
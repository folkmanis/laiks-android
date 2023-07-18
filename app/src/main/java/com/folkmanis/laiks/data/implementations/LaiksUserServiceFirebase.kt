package com.folkmanis.laiks.data.implementations

import android.util.Log
import com.folkmanis.laiks.USER_APPLIANCES_COLLECTION
import com.folkmanis.laiks.USER_COLLECTION
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.model.LaiksUser
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.utilities.NotLoggedInException
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
    private val firestore: FirebaseFirestore,
    private val accountService: AccountService,
) : LaiksUserService {

    private val collection = firestore.collection(USER_COLLECTION)

    private val uId
        get() = accountService.authUser?.uid ?: throw NotLoggedInException()

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

    override suspend fun userAppliance(uId: String, id: String): PowerAppliance? =
        collection.document(uId)
            .collection(USER_APPLIANCES_COLLECTION)
            .document(id)
            .get()
            .await()
            .toObject()

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
            .document(uId)
            .set(user)
            .await()
    }

    override suspend fun updateLaiksUser(update: HashMap<String, Any>) {
        collection
            .document(uId)
            .update(update)
            .await()
    }

    override suspend fun updateLaiksUser(key: String, value: Any) {
        updateLaiksUser(
            hashMapOf(
                key to value
            )
        )
    }

    override suspend fun setVatEnabled(userId: String, value: Boolean) {
        collection
            .document(userId)
            .update("includeVat", value)
            .await()
    }

    companion object {
        private const val TAG = "LaiksUserService"
    }
}
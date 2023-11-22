package com.folkmanis.laiks.data.implementations

import com.folkmanis.laiks.ADMINS_COLLECTION
import com.folkmanis.laiks.NP_BLOCKED_COLLECTION
import com.folkmanis.laiks.data.PermissionsService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PermissionsFirebase @Inject constructor(
    firestore: FirebaseFirestore,
) : PermissionsService {

    private val adminsCollection = firestore.collection(ADMINS_COLLECTION)
    private val npBlockedCollection = firestore.collection(NP_BLOCKED_COLLECTION)

    override fun isAdminFlow(uId: String): Flow<Boolean> = adminsCollection
        .document(uId)
        .snapshots()
        .map { snapshot -> snapshot.exists() }

    override suspend fun isAdmin(uId: String): Boolean = adminsCollection
        .document(uId)
        .get()
        .await()
        .exists()

    override fun npBlockedFlow(uId: String): Flow<Boolean> = npBlockedCollection
        .document(uId)
        .snapshots()
        .map { snapshot -> snapshot.exists() }

    override suspend fun npBlocked(uId: String): Boolean = npBlockedCollection
        .document(uId)
        .get()
        .await()
        .exists()

    companion object {
        @Suppress("unused")
        private const val TAG = "Permissions Service"
    }

}
package com.folkmanis.laiks.data.implementations

import com.folkmanis.laiks.PERMISSIONS_COLLECTION
import com.folkmanis.laiks.data.PermissionsService
import com.folkmanis.laiks.model.Permissions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PermissionsFirebase @Inject constructor(
    firestore: FirebaseFirestore,
) : PermissionsService {

    private val collection = firestore.collection(PERMISSIONS_COLLECTION)

    override suspend fun getPermissions(uId: String): Permissions =
        collection.document(uId)
            .get()
            .await()
            .toObject<Permissions>() ?: Permissions()

    override fun getPermissionFlow(uId: String, key: String): Flow<Boolean> =
        collection.document(uId)
            .snapshots()
            .map { snapshot-> snapshot.get(key) == true }

    override suspend fun updatePermission(uId: String, field: String, value: Any) {
        collection.document(uId)
            .update(field, value)
    }

    companion object {
        private const val TAG = "Permissions Service"
    }

}
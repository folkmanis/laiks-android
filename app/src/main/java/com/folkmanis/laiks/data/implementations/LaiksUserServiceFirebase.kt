package com.folkmanis.laiks.data.implementations

import android.util.Log
import com.folkmanis.laiks.USER_COLLECTION
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.AppliancesService
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.PermissionsService
import com.folkmanis.laiks.model.LaiksUser
import com.folkmanis.laiks.model.UserPowerAppliance
import com.folkmanis.laiks.utilities.NotLoggedInException
import com.google.firebase.auth.UserInfo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class LaiksUserServiceFirebase @Inject constructor(
    firestore: FirebaseFirestore,
    private val accountService: AccountService,
    private val appliancesService: AppliancesService,
    private val permissionsService: PermissionsService,
) : LaiksUserService {

    private val collection = firestore.collection(USER_COLLECTION)

    private val uId
        get() = accountService.authUser?.uid ?: throw NotLoggedInException()

    override val vatAmountFlow: Flow<Double>
        get() = laiksUserFlow().map { it?.tax ?: 1.0 }

    override val npAllowedFlow: Flow<Boolean>
        get() = accountService.firebaseUserFlow
            .flatMapLatest { user ->
                user?.let {
                    if (it.isEmailVerified)
                        permissionsService.npBlockedFlow(it.uid)
                    else null
                } ?: flowOf(true)
            }
            .map { npBlocked -> !npBlocked }

    override fun laiksUsersFlow(): Flow<List<LaiksUser>> = collection.snapshots().map { snapshot ->
        snapshot.toObjects()
    }

    override fun laiksUserFlow(): Flow<LaiksUser?> =
        accountService.firebaseUserFlow.flatMapLatest { user ->
            if (user == null) {
                flowOf(null)
            } else {
                laiksUserFlow(user.uid)
            }
        }

    override suspend fun laiksUser(): LaiksUser = accountService.authUser?.let {
        collection.document(it.uid).get().await().toObject()
    } ?: throw NotLoggedInException()

    override suspend fun userExists(id: String): Boolean =
        collection.document(id).get().await().exists()

    override suspend fun createLaiksUser(user: UserInfo) {

        val appliances = buildList {
            defaultAppliancesIds.forEach { id ->
                appliancesService.getAppliance(id)?.also {
                    add(UserPowerAppliance(it))
                }
            }
        }

        val laiksUser = LaiksUser(
            id = user.uid,
            name = user.displayName ?: "",
            email = user.email ?: "",
            verified = false,
            appliances = appliances,
        )
        Log.d(TAG, "new user: $user")
        collection.document(user.uid).set(laiksUser).await()
    }

    override suspend fun updateLaiksUser(user: LaiksUser) {
        collection.document(uId).set(user).await()
    }

    override suspend fun updateLaiksUser(update: HashMap<String, Any>) {
        collection.document(uId).update(update).await()
    }

    override suspend fun updateLaiksUser(key: String, value: Any) {
        updateLaiksUser(
            hashMapOf(
                key to value
            )
        )
    }

    override suspend fun setVatEnabled(value: Boolean) {
        collection.document(uId).update("includeVat", value).await()
    }

    private fun laiksUserFlow(uId: String): Flow<LaiksUser?> =
        collection.document(uId).snapshots().map { document ->
            document.toObject<LaiksUser>()
        }

    companion object {
        private const val TAG = "LaiksUserService"
        private val defaultAppliancesIds = listOf("washer", "dishwasher")
    }
}
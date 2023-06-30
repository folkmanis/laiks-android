package com.folkmanis.laiks.data

import com.folkmanis.laiks.model.LaiksUser
import com.folkmanis.laiks.model.PowerAppliance
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface LaiksUserService {

    fun laiksUserFlow(uId: String): Flow<LaiksUser?>

    fun laiksUsersFlow(): Flow<List<LaiksUser>>

    suspend fun laiksUser(uId: String): LaiksUser?

    suspend fun userAppliances(uId: String): List<PowerAppliance>

    suspend fun userAppliance(uId: String, id: String): PowerAppliance?

    suspend fun createLaiksUser(user: FirebaseUser)

    suspend fun updateLaiksUser(user: LaiksUser)

    suspend fun updateLaiksUser(uId: String, key: String, value: Any)

    suspend fun userExists(id: String): Boolean

    suspend fun setVatEnabled(userId: String, value: Boolean)

}
package com.folkmanis.laiks.data

import com.folkmanis.laiks.model.LaiksUser
import com.google.firebase.auth.UserInfo
import kotlinx.coroutines.flow.Flow

interface LaiksUserService {

    val vatAmountFlow: Flow<Double>

    val npAllowedFlow: Flow<Boolean>

    fun laiksUserFlow(): Flow<LaiksUser?>

    fun laiksUsersFlow(): Flow<List<LaiksUser>>

    suspend fun laiksUser(): LaiksUser

    suspend fun createLaiksUser(user: UserInfo)

    suspend fun updateLaiksUser(user: LaiksUser)

    suspend fun updateLaiksUser(key: String, value: Any)

    suspend fun updateLaiksUser(update: HashMap<String, Any>)

    suspend fun userExists(id: String): Boolean

    suspend fun setVatEnabled(value: Boolean)

}
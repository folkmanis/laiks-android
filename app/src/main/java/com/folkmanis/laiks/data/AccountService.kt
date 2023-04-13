package com.folkmanis.laiks.data

import com.folkmanis.laiks.model.LaiksUser
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AccountService {

    val firebaseUserFlow: Flow<FirebaseUser?>

    val authUser: FirebaseUser?

    val laiksUserFlow: Flow<LaiksUser?>

    fun laiksUserFlow(uId: String): Flow<LaiksUser?>
    fun laiksUsersFlow(): Flow<List<LaiksUser>>

    suspend fun laiksUser(uId: String): LaiksUser?
    suspend fun signWithEmailAndPassword(email: String, password: String)
    suspend fun createLaiksUser(user: FirebaseUser)

    suspend fun updateLaiksUser(user: LaiksUser)

    suspend fun userExists(id: String): Boolean

}
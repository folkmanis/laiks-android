package com.folkmanis.laiks.data

import com.folkmanis.laiks.model.LaiksUser
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AccountService {

    val currentUser: Flow<FirebaseUser?>

    suspend fun getLaiksUser(uId: String): Flow<LaiksUser?>
    suspend fun signWithEmailAndPassword(email: String, password: String)
    suspend fun signOut()

}
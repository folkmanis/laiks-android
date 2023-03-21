package com.folkmanis.laiks.data

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AccountService {

    val currentUser: Flow<FirebaseUser?>

    suspend fun signOut()

}
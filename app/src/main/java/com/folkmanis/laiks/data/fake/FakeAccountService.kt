@file:Suppress("RedundantNullableReturnType")

package com.folkmanis.laiks.data.fake

import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.model.LaiksUser
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


@Suppress("RedundantNullableReturnType")
class FakeAccountService : AccountService {

    override val authUser: FirebaseUser?
        get() = null

    override val firebaseUserFlow: Flow<FirebaseUser?>
        get() = flowOf(null)

    override suspend fun signWithEmailAndPassword(email: String, password: String) {

    }

}
package com.folkmanis.laiks.data.fake

import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.model.LaiksUser
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


class FakeAccountService : AccountService {

    override val laiksUserFlow: Flow<LaiksUser?>
        get() = flowOf(laiksUser)

    override val authUser: FirebaseUser?
        get() = null

    override val firebaseUserFlow: Flow<FirebaseUser?>
        get() = flowOf(null)

    override fun laiksUserFlow(uId: String): Flow<LaiksUser?> =
        flowOf(laiksUser)

    override suspend fun createLaiksUser(user: FirebaseUser) {

    }

    override suspend fun laiksUser(uId: String): LaiksUser? =
        laiksUser

    override fun laiksUsersFlow(): Flow<List<LaiksUser>> =
        flowOf(laiksUsers)

    override suspend fun userExists(id: String): Boolean = true

    override suspend fun signWithEmailAndPassword(email: String, password: String) {

    }

    override suspend fun updateLaiksUser(user: LaiksUser) {

    }

    companion object {
        val laiksUser: LaiksUser = LaiksUser(
            npAllowed = true,
            verified = true,
            name = "Simple User",
            id = "1234"
        )

        private val laiksAdmin = LaiksUser(
            name = "Admin User",
            id = "4321",
            npAllowed = true,
            isAdmin = true,
            verified = true,
        )

        val laiksUsers = listOf(laiksUser, laiksAdmin)
    }

}
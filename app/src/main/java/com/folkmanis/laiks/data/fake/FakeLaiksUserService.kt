package com.folkmanis.laiks.data.fake

import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.model.LaiksUser
import com.google.firebase.auth.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Suppress("RedundantNullableReturnType")
class FakeLaiksUserService : LaiksUserService {

    override val vatAmountFlow: Flow<Double>
        get() = flowOf(1.21)
    override fun laiksUserFlow(): Flow<LaiksUser?> =
        flowOf(laiksUser)

    override suspend fun createLaiksUser(user: UserInfo) {

    }

    override suspend fun laiksUser(): LaiksUser =
        laiksUser

    override fun laiksUsersFlow(): Flow<List<LaiksUser>> =
        flowOf(laiksUsers)

    override suspend fun userExists(id: String): Boolean = true

    override suspend fun updateLaiksUser(user: LaiksUser) {

    }

    override suspend fun updateLaiksUser(key: String, value: Any) {
        TODO("Not yet implemented")
    }

    override suspend fun updateLaiksUser(update: HashMap<String, Any>) {
        TODO("Not yet implemented")
    }
    override suspend fun setVatEnabled(value: Boolean) {
        TODO("Not yet implemented")
    }

    companion object {
        val laiksUser: LaiksUser = LaiksUser(
            verified = true,
            name = "Simple User",
            id = "1234",
            vatAmount = 0.21,
            includeVat = true,
        )

        private val laiksAdmin = LaiksUser(
            name = "Admin User",
            id = "4321",
            verified = true,
            vatAmount = 0.25,
            includeVat = true,
        )

        val laiksUsers = listOf(laiksUser, laiksAdmin)
    }


}
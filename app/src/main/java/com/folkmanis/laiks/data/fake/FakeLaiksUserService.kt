package com.folkmanis.laiks.data.fake

import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.model.LaiksUser
import com.folkmanis.laiks.model.PowerApplianceCycle
import com.folkmanis.laiks.model.UserPowerAppliance
import com.google.firebase.auth.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeLaiksUserService : LaiksUserService {

    override val vatAmountFlow: Flow<Double>
        get() = flowOf(1.21)

    override val npAllowedFlow: Flow<Boolean>
        get() = flowOf(true)

    override fun laiksUserFlow(): Flow<LaiksUser?> =
        flowOf(laiksUser)

    override suspend fun createLaiksUser(user: UserInfo) {

    }

    override suspend fun deleteLaiksUser() {

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
        private val dishWasher = UserPowerAppliance(
            cycles = listOf(
                PowerApplianceCycle(
                    consumption = 100,
                    length = 5 * 60 * 1000, // 5 min
                ),
                PowerApplianceCycle(
                    consumption = 2000,
                    length = 30 * 60 * 1000, // 30 min
                ),
                PowerApplianceCycle(
                    consumption = 150,
                    length = 40 * 60 * 1000, // 40 min
                ),
            ),
            name = "Trauku mašīna",
            delay = "start",
            minimumDelay = 0, // hours
            color = "#ff00ff",
        )

        private val washer = dishWasher.copy(
            delay = "end",
            minimumDelay = 3L,
            name = "Veļasmašīna"
        )

        private val testAppliances = listOf(
            dishWasher,
            washer
        )

        val laiksUser: LaiksUser = LaiksUser(
            verified = true,
            name = "Simple User",
            id = "1234",
            vatAmount = 0.21,
            includeVat = true,
            appliances = testAppliances,
        )

        private val laiksAdmin = LaiksUser(
            name = "Admin User",
            id = "4321",
            verified = true,
            vatAmount = 0.25,
            includeVat = true,
            appliances = testAppliances,
        )

        val laiksUsers = listOf(laiksUser, laiksAdmin)
    }


}
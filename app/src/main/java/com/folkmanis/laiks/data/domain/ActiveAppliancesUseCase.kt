package com.folkmanis.laiks.data.domain

import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.AppliancesService
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.model.ApplianceRecord
import com.folkmanis.laiks.model.ApplianceType
import com.folkmanis.laiks.model.PowerAppliance
import javax.inject.Inject

data class PowerApplianceRecord(
    val appliance: PowerAppliance,
    val type: Int,
) {
    val name: String
        get() = appliance.name
    val id: String
        get() = appliance.id
}

class ActiveAppliancesUseCase @Inject constructor(
    private val accountService: AccountService,
    private val appliancesService: AppliancesService,
    private val laiksUserService: LaiksUserService,
) {

    suspend operator fun invoke(): List<PowerApplianceRecord> {
        val uId = accountService.authUser?.uid ?: return emptyList()
        val user = laiksUserService.laiksUser(uId) ?: return emptyList()
        return resolveAppliances(
            applianceRecords = user.appliances,
            uId = uId,
        )
    }

    suspend operator fun invoke(type: Int, id: String): PowerAppliance? {
        return when (type) {
            ApplianceType.SYSTEM.type ->
                appliancesService.getAppliance(id)

            ApplianceType.USER.type -> {
                val uId = accountService.authUser?.uid ?: return null
                laiksUserService.userAppliance(uId, id)
            }

            else -> null
        }
    }

    private suspend fun resolveAppliances(
        applianceRecords: List<ApplianceRecord>,
        uId: String
    ): List<PowerApplianceRecord> =
        buildList {
            applianceRecords.forEach { record ->
                val appliance = when (record.type) {
                    ApplianceType.SYSTEM.type ->
                        appliancesService.getAppliance(record.applianceId)

                    ApplianceType.USER.type ->
                        laiksUserService.userAppliance(uId, record.applianceId)

                    else -> null
                }
                if (appliance != null)
                    add(PowerApplianceRecord(appliance, record.type))
            }
        }

}
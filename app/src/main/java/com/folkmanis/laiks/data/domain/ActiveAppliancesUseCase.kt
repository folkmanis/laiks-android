package com.folkmanis.laiks.data.domain

import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.AppliancesService
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.model.ApplianceRecord
import com.folkmanis.laiks.model.ApplianceType
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.model.PowerApplianceRecord
import javax.inject.Inject


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
                when (record.type) {
                    ApplianceType.SYSTEM.type ->
                        appliancesService.getAppliance(record.applianceId)

                    ApplianceType.USER.type ->
                        laiksUserService.userAppliance(uId, record.applianceId)

                    else -> null
                }?.let { appliance ->
                    add(PowerApplianceRecord(appliance, record.type))
                }
            }
        }

}
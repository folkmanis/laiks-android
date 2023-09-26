package com.folkmanis.laiks.data

import com.folkmanis.laiks.model.PresetPowerAppliance
import kotlinx.coroutines.flow.Flow

interface AppliancesService {

    suspend fun activeAppliances(): List<PresetPowerAppliance>

    val allAppliancesFlow: Flow<List<PresetPowerAppliance>>

    suspend fun getAppliance(id: String): PresetPowerAppliance?

    suspend fun updateAppliance(appliance: PresetPowerAppliance)

    suspend fun addAppliance(appliance: PresetPowerAppliance): String

    suspend fun deleteAppliance(id: String)

}
package com.folkmanis.laiks.data

import com.folkmanis.laiks.model.PowerAppliance
import kotlinx.coroutines.flow.Flow

interface AppliancesService {

    suspend fun activeAppliances(): List<PowerAppliance>

    val allAppliancesFlow: Flow<List<PowerAppliance>>

    suspend fun getAppliance(id: String): PowerAppliance?

    suspend fun updateAppliance(appliance: PowerAppliance)

    suspend fun addAppliance(appliance: PowerAppliance): String

    suspend fun deleteAppliance(id: String)

}
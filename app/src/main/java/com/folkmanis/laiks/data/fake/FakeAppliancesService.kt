package com.folkmanis.laiks.data.fake

import com.folkmanis.laiks.data.AppliancesService
import com.folkmanis.laiks.model.PresetPowerAppliance
import com.folkmanis.laiks.model.PowerApplianceCycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeAppliancesService : AppliancesService {
    override suspend fun addAppliance(appliance: PresetPowerAppliance): String {
        return "12ABF34"
    }

    override val allAppliancesFlow: Flow<List<PresetPowerAppliance>>
        get() = flowOf(testAppliances)

    override suspend fun getAppliance(id: String): PresetPowerAppliance? {
        return testAppliances.find { it.id == id }
    }

    override suspend fun updateAppliance(appliance: PresetPowerAppliance) {

    }

    override suspend fun deleteAppliance(id: String) {

    }

    override suspend fun activeAppliances(): List<PresetPowerAppliance> {
        return testAppliances
    }

    companion object {
        val dishWasher = PresetPowerAppliance(
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
            enabled = true,
            color = "#ff00ff",
            id = "12AFE34",
        )

        val washer = dishWasher.copy(
            id = "12AFE35",
            delay = "end",
            minimumDelay = 3L,
            name = "Veļasmašīna"
        )


        val testAppliances = listOf(
            dishWasher,
            washer
        )

    }
}
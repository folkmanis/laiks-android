package com.folkmanis.laiks.data.implementations

import com.folkmanis.laiks.POWER_APPLIANCES_COLLECTION
import com.folkmanis.laiks.data.AppliancesService
import com.folkmanis.laiks.model.PresetPowerAppliance
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AppliancesServiceFirebase @Inject constructor(
    firestore: FirebaseFirestore
) : AppliancesService {

    private val appliances = firestore
        .collection(POWER_APPLIANCES_COLLECTION)

    override val allAppliancesFlow: Flow<List<PresetPowerAppliance>>
        get() = appliances
            .orderBy("name")
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects()
            }

    override suspend fun activeAppliances(): List<PresetPowerAppliance> {
        return appliances
            .whereEqualTo("enabled", true)
            .orderBy("name")
            .get()
            .await()
            .toObjects()
    }

    override suspend fun getAppliance(id: String): PresetPowerAppliance? {
        return appliances
            .document(id)
            .get()
            .await()
            .toObject<PresetPowerAppliance>()
    }

    override suspend fun deleteAppliance(id: String) {
        appliances.document(id).delete().await()
    }

    override suspend fun updateAppliance(appliance: PresetPowerAppliance) {
        appliances.document(appliance.id).set(appliance).await()
    }

    override suspend fun addAppliance(appliance: PresetPowerAppliance): String {
        return appliances.add(appliance).await().id
    }

    companion object {
        @Suppress("unused")
        private const val TAG = "AppliancesServiceFirebase"
    }
}
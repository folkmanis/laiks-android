package com.folkmanis.laiks.data.implementations

import com.folkmanis.laiks.data.AppliancesService
import com.folkmanis.laiks.model.PowerAppliance
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AppliancesServiceFirebase @Inject constructor(
    firestore: FirebaseFirestore
)  : AppliancesService {

    private val appliances = firestore
        .collection(POWER_APPLIANCES_COLLECTION)

    override val allAppliancesFlow: Flow<List<PowerAppliance>>
        get() = appliances
            .orderBy("name")
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects()
            }

    override suspend fun activeAppliances(): List<PowerAppliance> {
        return appliances
            .whereEqualTo("enabled", true)
            .orderBy("name")
            .get()
            .await()
            .toObjects()
    }

    override suspend fun getAppliance(id: String): PowerAppliance? {
        return appliances
            .document(id)
            .get()
            .await()
            .toObject()
    }

    override suspend fun deleteAppliance(id: String) {
        appliances.document(id).delete().await()
    }

    override suspend fun updateAppliance(appliance: PowerAppliance) {
        appliances.document(appliance.id).set(appliance).await()
    }

    override suspend fun addAppliance(appliance: PowerAppliance): String {
        return appliances.add(appliance).await().id
    }

    companion object {
        private const val POWER_APPLIANCES_COLLECTION = "powerAppliances"
    }
}
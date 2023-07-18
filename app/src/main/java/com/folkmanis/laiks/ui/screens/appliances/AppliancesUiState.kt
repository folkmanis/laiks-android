package com.folkmanis.laiks.ui.screens.appliances

import com.folkmanis.laiks.model.PowerApplianceRecord

sealed interface AppliancesUiState {
    object Loading : AppliancesUiState
    data class Error(
        val reason: String,
        val exception: Throwable,
    ) : AppliancesUiState

    data class Success(
        val records: List<PowerApplianceRecord>
    ) : AppliancesUiState {
        fun removeRecordAt(idx: Int): Success {
            val updated = records.toMutableList()
            updated.removeAt(idx)
            return copy(records = updated)
        }

        fun addRecord(record:PowerApplianceRecord): Success {
            val updated = records.toMutableList()
            updated.add(record)
            return copy(records = updated)
        }
    }
}
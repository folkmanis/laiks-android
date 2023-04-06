package com.folkmanis.laiks.model

import androidx.annotation.StringRes
import com.folkmanis.laiks.R
import com.google.firebase.firestore.DocumentId

enum class PowerApplianceDelay(@StringRes val label: Int) {
    start(R.string.appliance_delay_start_label),
    end(R.string.appliance_delay_end_label)
}

data class PowerAppliance(
    @DocumentId val id: String = "",
    val color: String = "#CCCCCC",
    val cycles: List<PowerApplianceCycle> = emptyList(),
    val delay: String = "start",
    val enabled: Boolean = true,
    val minimumDelay: Long = 0,
    val name: String = "",
)

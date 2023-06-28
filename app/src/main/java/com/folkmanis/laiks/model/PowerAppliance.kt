package com.folkmanis.laiks.model

import androidx.annotation.StringRes
import com.folkmanis.laiks.R
import com.google.firebase.firestore.DocumentId
import java.util.Locale

@Suppress("EnumEntryName")
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
    val localizedNames: Map<String, String> = emptyMap(),
) {
    val localName: String
        get() {
            val locale = Locale.getDefault().language
            return localizedNames[locale] ?: name
        }
}

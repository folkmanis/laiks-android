package com.folkmanis.laiks.model

import androidx.annotation.StringRes
import com.folkmanis.laiks.R
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import java.util.Locale

@Suppress("EnumEntryName")
enum class PowerApplianceDelay(@StringRes val label: Int) {
    start(R.string.appliance_delay_start_label),
    end(R.string.appliance_delay_end_label)
}

data class PresetPowerAppliance(
    @DocumentId val id: String = "",
    override val color: String = "#CCCCCC",
    override val cycles: List<PowerApplianceCycle> = emptyList(),
    override val delay: String = "start",
    override val minimumDelay: Long = 0,
    override val name: String = "",
    val enabled: Boolean = true,
    val localizedNames: Map<String, String> = emptyMap(),
) : PowerAppliance {
    @get:Exclude
    val localName: String
        get() {
            val locale = Locale.getDefault().language
            return localizedNames[locale] ?: name
        }

}

data class UserPowerAppliance(
    override val color: String = "#CCCCCC",
    override val cycles: List<PowerApplianceCycle> = emptyList(),
    override val delay: String = "start",
    override val minimumDelay: Long = 0,
    override val name: String = "",
) : PowerAppliance {

    constructor(preset: PresetPowerAppliance) : this(
        color = preset.color,
        cycles = preset.cycles,
        delay = preset.delay,
        minimumDelay = preset.minimumDelay,
        name = preset.localName
    )
}

interface PowerAppliance {
    val color: String
    val cycles: List<PowerApplianceCycle>
    val delay: String
    val minimumDelay: Long
    val name: String
}

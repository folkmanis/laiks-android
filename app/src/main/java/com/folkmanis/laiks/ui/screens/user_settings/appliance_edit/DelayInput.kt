package com.folkmanis.laiks.ui.screens.user_settings.appliance_edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.model.PowerApplianceDelay


@Composable
fun DelayInput(
    delayType: String,
    onDelayTypeChange: (String) -> Unit,
    minimumDelay: Long?,
    onSetMinimumDelay: (Long?) -> Unit,
    minimumDelayValid: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier
    ) {

        DelayTypeSelection(
            value = delayType,
            onValueChange = onDelayTypeChange,
            enabled = enabled,
            modifier = Modifier.weight(1f),
        )

        NumberInput(
            value = minimumDelay,
            onValueChange = onSetMinimumDelay,
            modifier = Modifier.weight(1f),
            label = {
                Text(text = stringResource(id = R.string.appliance_minimumDelay_label))
            },
            isError = !minimumDelayValid,
            enabled = enabled,
            suffix = { Text(text = "h") }
        )

    }


}

@Composable
fun DelayTypeSelection(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
) {

    val delay = PowerApplianceDelay.valueOf(value)

    Column(
        modifier = modifier
            .selectableGroup()
    ) {
        PowerApplianceDelay.entries.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = option == delay,
                    onClick = { onValueChange(option.name) },
                    enabled = enabled,
                    modifier = Modifier.width(48.dp),
                )
                Text(
                    text = stringResource(id = option.label),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .clickable { onValueChange(option.name) }
                )
            }
        }
    }

}


@Preview
@Composable
fun DelayInputPreview() {
    MaterialTheme {
        DelayInput(
            delayType = PowerApplianceDelay.start.name,
            onDelayTypeChange = {},
            minimumDelay = 3,
            onSetMinimumDelay = {},
            minimumDelayValid = true,
            enabled = true,
        )
    }
}
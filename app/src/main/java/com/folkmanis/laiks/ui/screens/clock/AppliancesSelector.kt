package com.folkmanis.laiks.ui.screens.clock

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.folkmanis.laiks.data.fake.FakeAppliancesService
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.utilities.ext.contrasting

@Composable
fun AppliancesSelector(
    appliances: List<PowerAppliance>,
    onSelected: (idx: Int, name: String) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        appliances.forEachIndexed { idx, appliance ->
            val containerColor = Color(appliance.color.toColorInt())
            val colors = ButtonDefaults.elevatedButtonColors(
                containerColor = containerColor,
                contentColor = containerColor.contrasting()
            )
            Button(
                onClick = { onSelected(idx, appliance.name) },
                colors = colors,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(text = appliance.name)
            }
        }
    }

}

@Preview
@Composable
fun AppliancesSelectorPreview() {
    val appliances = FakeAppliancesService.testAppliances
    AppliancesSelector(
        appliances = appliances,
        onSelected = {_,_->}
    )
}
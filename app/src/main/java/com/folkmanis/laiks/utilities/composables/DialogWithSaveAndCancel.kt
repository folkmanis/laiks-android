package com.folkmanis.laiks.utilities.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R

@Composable
fun DialogWithSaveAndCancel(
    onCancel: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
    @StringRes   headingText:  Int,
    saveEnabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {

    DialogSurface {

        Column(
            modifier = modifier
                .fillMaxWidth(),
        ) {

            Text(
                text = stringResource(id = headingText),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(16.dp)
            )

            HorizontalDivider()

            content()

            HorizontalDivider()

            ButtonRow(
                onDismiss = onCancel,
                onAccept = onSave,
                saveEnabled = saveEnabled,
            )

        }

    }

}
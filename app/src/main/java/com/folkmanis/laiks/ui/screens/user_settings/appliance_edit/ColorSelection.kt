package com.folkmanis.laiks.ui.screens.user_settings.appliance_edit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.folkmanis.laiks.R

val AVAILABLE_COLORS: List<String> = listOf(
    "#ff00ff", "#12702b", "#1b1270", "#70121b", "#f7f307",
    "#23f707", "#919103", "#580391", "#000000"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorSelection(
    color: String,
    onColorChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    var showDialog by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .size(32.dp)
            .background(
                color = Color(color.toColorInt()),
                shape = CircleShape
            )
            .clickable(
                role = Role.Button,
                onClickLabel = stringResource(R.string.appliance_color_selection_dialog_open)
            ) { showDialog = true }

    )

    if (showDialog) {
        BasicAlertDialog(
            onDismissRequest = { showDialog = false },
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 3.dp,
            ) {
                ColorSelectionContent(
                    colors = AVAILABLE_COLORS,
                    onSelected = { color ->
                        showDialog = false
                        onColorChange(color)
                    },
                    onDismiss = { showDialog = false }
                )
            }
        }
    }


}

@Composable
fun ColorSelectionContent(
    colors: List<String>,
    onSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 72.dp),
            modifier = Modifier
                .padding(bottom = 56.dp)
        ) {
            items(colors) { color ->
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(
                            color = Color(color.toColorInt()),
                            shape = MaterialTheme.shapes.small
                        )
                        .size(48.dp)
                        .clickable { onSelected(color) }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.BottomCenter)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.action_cancel)
                    )
                }
            }
        }

    }

}

@Preview
@Composable
fun ColorSelectionContentPreview() {
    MaterialTheme {
        ColorSelectionContent(
            colors = AVAILABLE_COLORS,
            onSelected = {},
            onDismiss = { },
            modifier = Modifier
                .fillMaxWidth(0.8f)

        )
    }
}
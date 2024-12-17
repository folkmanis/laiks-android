package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.folkmanis.laiks.R
import com.folkmanis.laiks.utilities.composables.DialogWithSaveAndCancel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeMarkupInputDialog(
    initialValue: Double?,
    onAccept: (Double) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var markup by remember {
        mutableStateOf(initialValue)
    }

    val saveEnabled by remember(markup, initialValue) {
        derivedStateOf {
            markup != null && markup != initialValue
        }
    }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier
    ) {
        TradeMarkupInputScreen(
            tradeMarkup = markup,
            onTradeMarkupChange = { markup = it },
            onDismiss = onDismiss,
            onAccept = {
                val value = markup
                if (value != null) {
                    onAccept(value)
                } else {
                    onDismiss()
                }
            },
            saveEnabled = saveEnabled,
            modifier = modifier
        )
    }
}

@Composable()
fun TradeMarkupInputScreen(
    tradeMarkup: Double?,
    onTradeMarkupChange: (Double?) -> Unit,
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
    modifier: Modifier = Modifier,
    saveEnabled: Boolean = false,
) {
DialogWithSaveAndCancel(
    onCancel = onDismiss,
    onSave = onAccept, headingText = R.string.trade_markup_amount,
    modifier = modifier,
    saveEnabled = saveEnabled,) {

}
}
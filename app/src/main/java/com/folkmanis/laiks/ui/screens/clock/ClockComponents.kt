package com.folkmanis.laiks.ui.screens.clock

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.STEP_DOWN_VALUE
import com.folkmanis.laiks.STEP_UP_VALUE
import com.folkmanis.laiks.ui.theme.LaiksTheme
import com.folkmanis.laiks.utilities.ext.toSignedString

@Composable
fun LargeVerticalUpDownButtons(
    offset: Int,
    onOffsetChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {

        LargeOffsetChangeButton(
            onClick = { onOffsetChange(STEP_UP_VALUE) },
            iconId = R.drawable.add,
            descriptionId = R.string.hour_plus_button,
        )

        OffsetHours(
            offset,
            modifier = Modifier.padding(vertical = 8.dp),
            fontSize = 64.sp,
        )

        LargeOffsetChangeButton(
            onClick = { onOffsetChange(STEP_DOWN_VALUE) },
            iconId = R.drawable.remove,
            descriptionId = R.string.hour_minus_button,
        )

    }
}

@Composable
fun VerticalUpDownButtons(
    offset: Int,
    onOffsetChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {

        OffsetChangeButton(
            onClick = { onOffsetChange(STEP_UP_VALUE) },
            iconId = R.drawable.add,
            descriptionId = R.string.hour_plus_button,
        )

        OffsetHours(
            offset,
            modifier = Modifier.padding(vertical = 8.dp),
            fontSize = 64.sp,
        )

        OffsetChangeButton(
            onClick = { onOffsetChange(STEP_DOWN_VALUE) },
            iconId = R.drawable.remove,
            descriptionId = R.string.hour_minus_button,
        )

    }
}

@Composable
fun HorizontalUpDownButtons(
    offset: Int,
    onOffsetChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {

        LargeOffsetChangeButton(
            onClick = { onOffsetChange(STEP_DOWN_VALUE) },
            iconId = R.drawable.remove,
            descriptionId = R.string.hour_minus_button,
        )

        OffsetHours(
            offset,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .wrapContentWidth(),
            fontSize = 64.sp,
        )

        LargeOffsetChangeButton(
            onClick = { onOffsetChange(STEP_UP_VALUE) },
            iconId = R.drawable.add,
            descriptionId = R.string.hour_plus_button,
        )

    }
}

@Composable
fun OffsetHours(
    offset: Int,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
) {
    Text(
        text = offset.toSignedString(),
        fontSize = fontSize,
        fontWeight = FontWeight.ExtraBold,
        modifier = modifier
    )
}

@Composable
fun LargeOffsetChangeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes iconId: Int,
    @StringRes descriptionId: Int,
) {
    LargeFloatingActionButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(iconId),
            contentDescription = stringResource(descriptionId),
        )
    }
}

@Composable
fun OffsetChangeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes iconId: Int,
    @StringRes descriptionId: Int,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(iconId),
            contentDescription = stringResource(descriptionId),
        )
    }
}

@Composable
fun TimeComponent(
    hours: String,
    minutes: String,
    modifier: Modifier = Modifier
) {

    Row(modifier = modifier) {

        TimeSymbols(text = hours)

        TimeSymbols(
            text = stringResource(id = R.string.minutes_separator),
        )

        TimeSymbols(text = minutes)

    }
}

@Composable
fun TimeSymbols(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        modifier = modifier,
    )
}

@Preview
@Composable
fun UpDownButtonsPreviewVertical() {
    LaiksTheme {
        LargeVerticalUpDownButtons(
            offset = 2,
            onOffsetChange = {}
        )
    }
}

@Preview
@Composable
fun UpDownButtonsPreviewHorizontal() {
    LaiksTheme {
        HorizontalUpDownButtons(
            offset = 2,
            onOffsetChange = {},
        )
    }
}

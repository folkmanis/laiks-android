package com.folkmanis.laiks.ui.screens.clock

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
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
            modifier = modifier.zIndex(1f),
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
            modifier = modifier.zIndex(1f),
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
            modifier = Modifier.zIndex(1f),
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
            modifier = Modifier.zIndex(1f),
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
    AnimatedContent(
        targetState = offset,
        modifier = modifier,
        label = "Offset Hours",
        transitionSpec = {
            if (targetState > initialState) {
                slideInVertically { height -> height } + fadeIn() togetherWith
                        slideOutVertically { height -> -height } + fadeOut()
            } else {
                slideInVertically { height -> -height } + fadeIn() togetherWith
                        slideOutVertically { height -> height } + fadeOut()
            }.using(
                SizeTransform(clip = false)
            )
        }
    ) { targetOffset ->
        Text(
            text = targetOffset.toSignedString(),
            fontSize = fontSize,
            fontWeight = FontWeight.ExtraBold,
        )
    }
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
        modifier = modifier,
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

@Composable
fun PricesButton(
    onClick: () -> Unit,
    isLarge: Boolean,
    modifier: Modifier = Modifier,
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        expanded = isLarge,
        icon = {
            Icon(
                painter = painterResource(R.drawable.euro_symbol_48px),
                contentDescription = stringResource(R.string.show_prices_button),
                modifier = Modifier.size(24.dp),
            )
        },
        text = {
            Text(
                text = stringResource(R.string.show_prices_button),
            )
        }
    )

}

@Preview
@Composable
fun UpDownButtonsPreviewVertical() {
    var offset by remember {
        mutableIntStateOf(2)
    }
    LaiksTheme {
        LargeVerticalUpDownButtons(
            offset = offset,
            onOffsetChange = { offset += it }
        )
    }
}

@Preview
@Composable
fun UpDownButtonsPreviewHorizontal() {
    var offset by remember {
        mutableIntStateOf(2)
    }
    LaiksTheme {
        HorizontalUpDownButtons(
            offset = offset,
            onOffsetChange = { offset += it }
        )
    }
}

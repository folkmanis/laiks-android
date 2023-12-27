package com.folkmanis.laiks.ui.screens.clock

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable

fun Placeable.centerX(center: Int): Int =
    center - width / 2

@Composable
fun VerticalClockLayout(
    modifier: Modifier = Modifier,
    top: @Composable () -> Unit,
    bottom: @Composable () -> Unit,
    large: @Composable () -> Unit,
    normal: @Composable () -> Unit,
) {
    Layout(
        content = {
            top()
            bottom()
            large()
            normal()
        },
        modifier = modifier
    ) { measurables, constraints ->

        val topPlaceable = measurables[0].measure(constraints)
        val bottomPlaceable = measurables[1].measure(constraints)

        val largePlaceable = measurables[2].measure(constraints)

        val isLarge =
            largePlaceable.height + topPlaceable.height * 2 <= constraints.maxHeight

        val selectorPlaceable = if (isLarge) largePlaceable else {
            measurables[3].measure(constraints)
        }

        val totalWidth = maxOf(topPlaceable.width, bottomPlaceable.width, selectorPlaceable.width)
            .coerceIn(constraints.minWidth, constraints.maxWidth)
        val totalHeight = constraints.maxHeight

        layout(totalWidth, totalHeight) {

            val horizontalCenter = totalWidth / 2

            val topBottomHeight = (totalHeight - selectorPlaceable.height) / 2

            with(selectorPlaceable) {
                place(
                    centerX(horizontalCenter),
                    topBottomHeight
                )
            }


            with(topPlaceable) {
                place(
                    centerX(horizontalCenter),
                    (topBottomHeight - height) / 2
                )
            }

            with(bottomPlaceable) {
                val offsetY = totalHeight - topBottomHeight
                place(
                    centerX(horizontalCenter),
                    offsetY + (topBottomHeight - height) / 2
                )
            }

        }
    }
}

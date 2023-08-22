package com.vallem.componentlibrary.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vallem.componentlibrary.ui.chip.SylphChip
import com.vallem.componentlibrary.ui.chip.SylphChipDefaults
import com.vallem.componentlibrary.ui.theme.SylphTheme

// based on https://stackoverflow.com/questions/71390324/jetpack-compose-detect-child-item-overflow-and-add-more-badge
object SylphOverflowList {
    enum class Orientation {
        Vertical, Horizontal
    }

    private data class Element(val placeable: Placeable, val position: Int)

    @Composable
    operator fun invoke(
        orientation: Orientation,
        modifier: Modifier = Modifier,
        content: @Composable (Int) -> Unit
    ) {
        var renderedElements by remember { mutableIntStateOf(0) }

        if (orientation == Orientation.Horizontal) HorizontalList(
            onPlacementComplete = { renderedElements = it },
            modifier = modifier,
            content = { content(renderedElements) }
        )
        else VerticalList(
            onPlacementComplete = { renderedElements = it },
            modifier = modifier,
            content = { content(renderedElements) }
        )
    }

    @Composable
    private fun HorizontalList(
        onPlacementComplete: (Int) -> Unit,
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {
        Box(modifier = modifier) {
            Layout(content = content) { measurables, constraints ->
                if (measurables.isEmpty()) return@Layout layout(0, 0) {}

                val elements = mutableListOf<Element>()
                var xPosition = 0

                val lastItem = measurables.lastOrNull()?.measure(constraints)
                val lastItemWidth = lastItem?.width ?: 0
                val maxWidth = constraints.maxWidth - lastItemWidth

                for (i in 0 until measurables.lastIndex) {
                    val placeable = measurables[i].measure(constraints)
                    if (xPosition + placeable.width > maxWidth) break

                    elements.add(Element(placeable, xPosition))
                    xPosition += placeable.width
                }

                lastItem?.let { elements.add(Element(it, xPosition)) }

                layout(
                    width = elements.lastOrNull()?.let { it.position + it.placeable.width } ?: 0,
                    height = elements.maxOfOrNull { it.placeable.height } ?: 0
                ) {
                    elements.forEach { it.placeable.place(it.position, 0) }
                    onPlacementComplete(elements.size)
                }
            }
        }
    }

    @Composable
    private fun VerticalList(
        onPlacementComplete: (Int) -> Unit,
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {
        Box(modifier = modifier) {
            Layout(content = content) { measurables, constraints ->
                if (measurables.isEmpty()) return@Layout layout(0, 0) {}

                val elements = mutableListOf<Element>()
                var yPosition = 0

                val lastItem = measurables.lastOrNull()?.measure(constraints)
                val lastItemHeight = lastItem?.height ?: 0
                val maxHeight = constraints.maxHeight - lastItemHeight

                for (i in 0 until measurables.lastIndex) {
                    val placeable = measurables[i].measure(constraints)
                    if (yPosition + placeable.height > maxHeight) break

                    elements.add(Element(placeable, yPosition))
                    yPosition += placeable.height
                }

                lastItem?.let { elements.add(Element(it, yPosition)) }

                layout(
                    height = elements.lastOrNull()?.let { it.position + it.placeable.height } ?: 0,
                    width = elements.maxOfOrNull { it.placeable.width } ?: 0
                ) {
                    elements.forEach { it.placeable.place(0, it.position) }
                    onPlacementComplete(elements.size)
                }
            }
        }
    }
}

@Preview
@Composable
private fun OverflowRowPreview() {
    val itemsToBeRendered = 10

    SylphTheme {
        SylphOverflowList(
            SylphOverflowList.Orientation.Horizontal,
            modifier = Modifier
                .width(300.dp)
                .height(210.dp)
        ) { elements ->
            repeat(itemsToBeRendered) {
                SylphChip.Small(
                    text = it.toString(),
                    modifier = Modifier.padding(end = 8.dp, bottom = 8.dp)
                )
            }

            if (elements < itemsToBeRendered) SylphChip.Small(
                text = "${itemsToBeRendered - elements + 1}+",
                colors = SylphChipDefaults.primaryColors()
            )
        }
    }
}
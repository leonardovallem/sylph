package com.vallem.sylph.events.presentation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun <T> DoubleRow(
    items: List<T>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    component: @Composable (T) -> Unit
) {
    val layoutDirection = LocalLayoutDirection.current

    val lists = remember {
        if (items.size % 2 == 0) listOf(
            items.subList(0, items.size / 2),
            items.subList(items.size / 2, items.size)
        ) else listOf(
            items.subList(0, items.size / 2 + 1),
            items.subList(items.size / 2 + 1, items.size)
        )
    }

    Column(
        verticalArrangement = verticalArrangement,
        modifier = modifier
            .width(IntrinsicSize.Min)
            .padding(
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding()
            )
    ) {
        lists.forEach { list ->
            Row(
                horizontalArrangement = horizontalArrangement,
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                Spacer(
                    modifier = Modifier.width(contentPadding.calculateLeftPadding(layoutDirection))
                )

                list.forEach { component(it) }

                Spacer(
                    modifier = Modifier.width(contentPadding.calculateRightPadding(layoutDirection))
                )
            }
        }
    }
}
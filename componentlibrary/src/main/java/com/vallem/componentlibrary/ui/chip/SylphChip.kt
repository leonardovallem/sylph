package com.vallem.componentlibrary.ui.chip

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vallem.componentlibrary.ui.theme.SylphTheme

object SylphChip {
    @Composable
    fun Primary(
        text: String,
        modifier: Modifier = Modifier,
        onClick: (() -> Unit)? = null,
        selected: Boolean = false,
        colors: SylphChipColors = SylphChipDefaults.primaryColors(),
    ) {
        val borderColor by animateColorAsState(
            targetValue = if (selected) colors.border else colors.container,
            label = "PrimaryBorderColor"
        )

        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = colors.content,
            textAlign = TextAlign.Center,
            modifier = modifier
                .clip(RoundedCornerShape(24.dp))
                .clickable(enabled = onClick != null) { onClick?.invoke() }
                .background(colors.container)
                .border(
                    width = 2.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }

    @Composable
    fun Small(
        text: String,
        modifier: Modifier = Modifier,
        onClick: (() -> Unit)? = null,
        selected: Boolean = false,
        colors: SylphChipColors = SylphChipDefaults.smallColors(),
    ) {
        val borderColor by animateColorAsState(
            targetValue = if (selected) colors.border else colors.container,
            label = "PrimaryBorderColor"
        )

        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = colors.content,
            textAlign = TextAlign.Center,
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable(enabled = onClick != null) { onClick?.invoke() }
                .background(colors.container)
                .border(
                    width = 2.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

@Preview(name = "Primary")
@Composable
private fun PrimarySylphChipPreview() {
    SylphTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SylphChip.Primary(text = "No Action")
            SylphChip.Primary(text = "With Action", onClick = {})
            SylphChip.Primary(text = "Selected", onClick = {}, selected = true)
        }
    }
}

@Preview(name = "Small")
@Composable
private fun SmallSylphChipPreview() {
    SylphTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SylphChip.Small(text = "No Action")
            SylphChip.Small(text = "With Action", onClick = {})
            SylphChip.Small(text = "Selected", onClick = {}, selected = true)
        }
    }
}
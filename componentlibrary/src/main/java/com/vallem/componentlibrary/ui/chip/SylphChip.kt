package com.vallem.componentlibrary.ui.chip

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vallem.componentlibrary.ui.theme.SylphTheme

object SylphChip {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Primary(
        text: String,
        modifier: Modifier = Modifier,
        onClick: (() -> Unit)? = null,
        selected: Boolean = false,
        colors: SylphChipColors = SylphChipDefaults.primaryColors(),
    ) {
        Surface(
            onClick = { onClick?.invoke() },
            enabled = onClick != null,
            color = colors.container,
            contentColor = colors.content,
            shape = RoundedCornerShape(24.dp),
            modifier = modifier.run {
                if (selected) border(2.dp, colors.border, RoundedCornerShape(24.dp))
                else this
            }
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = colors.content,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }
}

@Preview
@Composable
private fun SylphChipsPreview() {
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
package com.vallem.componentlibrary.ui.button

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vallem.componentlibrary.ui.theme.SylphTheme

object SylphButton {
    @Composable
    fun Pill(
        label: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        isLoading: Boolean = false,
    ) {
        FilledTonalButton(
            onClick = onClick,
            enabled = enabled && !isLoading,
            modifier = modifier,
        ) {
            AnimatedContent(
                targetState = isLoading,
                label = "SylphPillButtonAnimatedContent",
                contentAlignment = Alignment.Center
            ) {
                if (it) CircularProgressIndicator()
                else Text(text = label)
            }
        }
    }

    @Composable
    fun Elevated(
        label: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        colors: SylphButtonColors = SylphButtonDefaults.elevatedColors(),
        enabled: Boolean = true,
        isLoading: Boolean = false,
    ) {
        ElevatedButton(
            onClick = onClick,
            enabled = enabled,
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = colors.container,
                contentColor = colors.content,
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = modifier.heightIn(min = 48.dp),
        ) {
            AnimatedContent(
                targetState = isLoading,
                label = "SylphElevatedButtonAnimatedContent",
                contentAlignment = Alignment.Center
            ) {
                if (it) CircularProgressIndicator()
                else Text(
                    text = label,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview
@Composable
private fun SylphButtonPreview() {
    var loadingIndex by remember { mutableIntStateOf(-1) }

    SylphTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp),
        ) {
            SylphButton.Pill(
                label = "Pill",
                onClick = { loadingIndex = 0 },
                isLoading = loadingIndex == 0
            )

            SylphButton.Elevated(
                label = "Elevated",
                onClick = { loadingIndex = 1 },
                isLoading = loadingIndex == 1
            )
        }
    }
}
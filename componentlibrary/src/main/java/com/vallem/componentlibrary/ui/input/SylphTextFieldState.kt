package com.vallem.componentlibrary.ui.input

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vallem.componentlibrary.ui.theme.SylphTheme

enum class SylphTextFieldState {
    Default, Success, Error, Warning;

    companion object {
        val values = values()
    }
}

fun SylphTextFieldState.Companion.successIf(condition: Boolean) = when {
    condition -> SylphTextFieldState.Success
    else -> SylphTextFieldState.Default
}

fun SylphTextFieldState.Companion.errorIf(condition: Boolean) = when {
    condition -> SylphTextFieldState.Error
    else -> SylphTextFieldState.Default
}

fun SylphTextFieldState.Companion.warningIf(condition: Boolean) = when {
    condition -> SylphTextFieldState.Warning
    else -> SylphTextFieldState.Default
}

val SylphTextFieldState.textColor: Color
    @Composable get() = when (this@textColor) {
        SylphTextFieldState.Default -> MaterialTheme.colorScheme.outline
        SylphTextFieldState.Success -> SylphTheme.colorScheme.Success
        SylphTextFieldState.Error -> MaterialTheme.colorScheme.error
        SylphTextFieldState.Warning -> SylphTheme.colorScheme.Warning
    }

val SylphTextFieldState.backgroundColor: Color
    @Composable get() = when (this@backgroundColor) {
        SylphTextFieldState.Default -> MaterialTheme.colorScheme.surfaceVariant
        SylphTextFieldState.Success -> SylphTheme.colorScheme.SuccessContainer
        SylphTextFieldState.Error -> MaterialTheme.colorScheme.errorContainer
        SylphTextFieldState.Warning -> SylphTheme.colorScheme.WarningContainer
    }

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun StateColorsPreview() {
    SylphTheme {
        FlowRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SylphTextFieldState.values.forEach {
                Text(
                    text = it.toString(),
                    color = it.textColor,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .background(it.backgroundColor, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                )
            }
        }
    }
}
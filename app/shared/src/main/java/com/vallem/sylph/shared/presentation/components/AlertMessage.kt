package com.vallem.sylph.shared.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Celebration
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vallem.componentlibrary.ui.theme.SylphTheme
import com.vallem.componentlibrary.ui.theme.zoneEventColors

@Composable
fun AlertMessage(
    title: String,
    description: String,
    level: AlertLevel,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier,
    ) {
        Icon(
            imageVector = level.icon,
            contentDescription = null,
            tint = level.color,
            modifier = Modifier.size(48.dp)
        )

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}

enum class AlertLevel {
    Success, Error, Warning
}

private val AlertLevel.icon: ImageVector
    get() = with(Icons.Rounded) {
        when (this@icon) {
            AlertLevel.Success -> Celebration
            AlertLevel.Error -> ErrorOutline
            AlertLevel.Warning -> WarningAmber
        }
    }

private val AlertLevel.color: Color
    @Composable get() = when (this@color) {
        AlertLevel.Success -> MaterialTheme.zoneEventColors.safety
        AlertLevel.Error -> MaterialTheme.zoneEventColors.danger
        AlertLevel.Warning -> Color.Yellow
    }

@Preview
@Composable
private fun AlertMessagePreview() {
    SylphTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(24.dp)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            AlertMessage(
                title = "Evento criado com sucesso!",
                description = "O evento foi publicado e em breve estará no mapa.",
                level = AlertLevel.Success
            )

            AlertMessage(
                title = "Deseja prosseguir?",
                description = "A ação não poderá ser desfeita.",
                level = AlertLevel.Warning
            )

            AlertMessage(
                title = "Algo deu errado...",
                description = "Ocorreu um erro ao recuperar os detalhes desse evento.",
                level = AlertLevel.Error
            )
        }
    }
}
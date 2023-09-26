package com.vallem.sylph.events.presentation.detail

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vallem.componentlibrary.ui.bottomsheet.SylphBottomSheet
import com.vallem.componentlibrary.ui.theme.SylphTheme
import com.vallem.componentlibrary.ui.theme.TransFlagColors
import com.vallem.sylph.shared.presentation.components.AlertLevel
import com.vallem.sylph.shared.presentation.components.AlertMessage

@Composable
internal fun DetailsLoading(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "")
    val color = transition.animateColor(
        initialValue = TransFlagColors.Pink,
        targetValue = TransFlagColors.Blue,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        CircularProgressIndicator(
            color = color.value,
            strokeCap = StrokeCap.Round,
            strokeWidth = 6.dp
        )
    }
}

@Composable
internal fun DetailsRetrievalError(modifier: Modifier = Modifier) {
    AlertMessage(
        title = "Algo deu errado...",
        description = "Ocorreu um erro ao recuperar os detalhes desse evento.",
        level = AlertLevel.Error,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Preview
@Composable
private fun DetailsLoadingPreview() {
    SylphTheme {
        SylphBottomSheet {
            DetailsLoading(Modifier.padding(it))
        }
    }
}

@Preview
@Composable
private fun DetailsErrorPreview() {
    SylphTheme {
        SylphBottomSheet {
            DetailsRetrievalError(Modifier.padding(it))
        }
    }
}
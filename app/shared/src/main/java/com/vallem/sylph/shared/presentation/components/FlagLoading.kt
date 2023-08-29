package com.vallem.sylph.shared.presentation.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.vallem.componentlibrary.ui.theme.TransFlagColors

@Composable
fun FlagLoading(modifier: Modifier = Modifier) {
    val gradient = listOf(
        Color.LightGray.copy(alpha = 0.0f),
        Color.LightGray.copy(alpha = 0.75f),
        Color.LightGray.copy(alpha = 0.0f),
    )

    val transition = rememberInfiniteTransition(label = "")

    BoxWithConstraints(modifier = modifier) {
        val translation by transition.animateFloat(
            initialValue = 0f,
            targetValue = 10 * maxWidth.value,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1000
                ),
            ),
            label = "Shimmer"
        )

        val brush = linearGradient(
            colors = gradient,
            start = Offset.Zero,
            end = Offset(x = translation, y = translation)
        )

        Box(
            modifier = Modifier
                .background(brush)
                .width(maxWidth)
                .height(maxHeight)
                .zIndex(10f)
        )

        Column(
            modifier = modifier
                .width(maxWidth)
                .height(maxHeight)
        ) {
            Box(
                modifier = Modifier
                    .background(TransFlagColors.Pink.copy(alpha = 0.5f))
                    .fillMaxWidth()
                    .weight(1f)
            )
            Box(
                modifier = Modifier
                    .background(TransFlagColors.White.copy(alpha = 0.5f))
                    .fillMaxWidth()
                    .weight(1f)
            )
            Box(
                modifier = Modifier
                    .background(TransFlagColors.Blue.copy(alpha = 0.5f))
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}

@Preview
@Composable
private fun FlagLoadingPreview() {
    FlagLoading(
        modifier = Modifier
            .width(500.dp)
            .height(200.dp)
    )
}

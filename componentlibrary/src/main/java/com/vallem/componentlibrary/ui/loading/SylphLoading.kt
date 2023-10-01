package com.vallem.componentlibrary.ui.loading

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.vallem.componentlibrary.ui.theme.SylphTheme
import com.vallem.componentlibrary.ui.theme.TransFlagColors

object SylphLoading {
    @Composable
    fun Linear(
        modifier: Modifier = Modifier,
        barHeight: Dp = 2.dp,
        barWidthPercentage: Float = 0.3f,
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "")

        BoxWithConstraints(modifier = modifier.background(TransFlagColors.White)) {
            val progress by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = maxWidth.value - maxWidth.value * barWidthPercentage,
                animationSpec = infiniteRepeatable(
                    tween(1000),
                    repeatMode = RepeatMode.Reverse
                ),
                label = ""
            )

            val color by infiniteTransition.animateColor(
                initialValue = TransFlagColors.Pink,
                targetValue = TransFlagColors.Blue,
                animationSpec = infiniteRepeatable(
                    tween(1000),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "",
            )

            Box(modifier = Modifier.offset(x = progress.dp)) {
                Box(
                    modifier = Modifier
                        .background(color)
                        .width(this@BoxWithConstraints.maxWidth * barWidthPercentage)
                        .height(barHeight)
                        .zIndex(0f)
                )
            }
        }
    }

    @Composable
    fun Circular(
        modifier: Modifier = Modifier,
        strokeCap: StrokeCap = StrokeCap.Round,
        strokeWidth: Dp = 6.dp,
    ) {
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

        CircularProgressIndicator(
            color = color.value,
            strokeCap = strokeCap,
            strokeWidth = strokeWidth,
            modifier = modifier,
        )
    }
}

@Preview
@Composable
private fun LinearSylphLoadingPreview() {
    SylphTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(Color(0xFF161616))
                .padding(24.dp),
        ) {
            SylphLoading.Linear(
                modifier = Modifier.fillMaxWidth(),
                barHeight = 12.dp
            )
        }
    }
}

@Preview
@Composable
private fun CircularSylphLoadingPreview() {
    SylphTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(Color(0xFF161616))
                .padding(24.dp),
        ) {
            SylphLoading.Circular()
        }
    }
}

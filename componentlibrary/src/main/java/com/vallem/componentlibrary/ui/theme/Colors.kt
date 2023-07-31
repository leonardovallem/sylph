package com.vallem.componentlibrary.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

sealed interface Colors {
    val Primary: Color
    val Secondary: Color
    val Background: Color
    val Surface: Color
    val OnPrimary: Color
    val OnSecondary: Color
    val OnBackground: Color
    val OnSurface: Color

    val SafetyGreen: Color
    val TransgenderFlagPink: Color
    val TransgenderFlagWhite: Color
    val TransgenderFlagLightBlue: Color
    val TransgenderFlagMediumBlue: Color
    val TransgenderFlagDarkBlue: Color

    val Success: Color
    val SuccessContainer: Color

    val Warning: Color
    val WarningContainer: Color

    fun toColorScheme(): ColorScheme

    object Light : Colors {
        override val Primary = Color(0xFF3F51B5)
        override val Secondary = Color(0xFFFFC107)
        override val Background = Color(0xFFF5F5F5)
        override val Surface = Color.White
        override val OnPrimary = Color.White
        override val OnSecondary = Color.Black
        override val OnBackground = Color.Black
        override val OnSurface = Color.Black

        override val SafetyGreen = Color(0xFF6AB344)
        override val TransgenderFlagPink = Color(0xFF5BCEFA)
        override val TransgenderFlagWhite = Color.White
        override val TransgenderFlagLightBlue = Color(0xFFFF5A9B)
        override val TransgenderFlagMediumBlue = Color(0xFF9B4E8F)
        override val TransgenderFlagDarkBlue = Color(0xFF5BCEFA)

        override val Success = Color(0xFF9AE374)
        override val SuccessContainer = Color(0xFF2AB304)
        override val Warning = Color(0xFFFFAAA0)
        override val WarningContainer = Color(0xFFFF4A40)

        override fun toColorScheme() = lightColorScheme(
            primary = Primary,
            onPrimary = OnPrimary,
            background = Background,
            onBackground = OnBackground,
            surface = Surface,
            onSurface = OnSurface,
            secondary = Secondary,
            onSecondary = OnSecondary,
        )
    }

    object Dark : Colors {
        override val Primary = Color(0xFF9FA8DA)
        override val Secondary = Color(0xFFFFD54F)
        override val Background = Color(0xFF121212)
        override val Surface = Color(0xFF1E1E1E)
        override val OnPrimary = Color.White
        override val OnSecondary = Color.Black
        override val OnBackground = Color.White
        override val OnSurface = Color.White

        override val SafetyGreen = Color(0xFF8BC34A)
        override val TransgenderFlagPink = Color(0xFF81D4FA)
        override val TransgenderFlagWhite = Color.White
        override val TransgenderFlagLightBlue = Color(0xFFFF8A80)
        override val TransgenderFlagMediumBlue = Color(0xFFB39DDB)
        override val TransgenderFlagDarkBlue = Color(0xFF81D4FA)

        override val Success = Color(0xFF2AB304)
        override val SuccessContainer = Color(0xFF9AE374)
        override val Warning = Color(0xFFFF4A60)
        override val WarningContainer = Color(0xFFFFAAA0)

        override fun toColorScheme() = lightColorScheme(
            primary = Primary,
            onPrimary = OnPrimary,
            background = Background,
            onBackground = OnBackground,
            surface = Surface,
            onSurface = OnSurface,
            secondary = Secondary,
            onSecondary = OnSecondary,
        )
    }
}

fun Colors.all() = listOf(
    "Primary" to Primary,
    "Secondary" to Secondary,
    "Background" to Background,
    "Surface" to Surface,
    "OnPrimary" to OnPrimary,
    "OnSecondary" to OnSecondary,
    "OnBackground" to OnBackground,
    "OnSurface" to OnSurface,
    "SafetyGreen" to SafetyGreen,
    "TransgenderFlag\nPink" to TransgenderFlagPink,
    "TransgenderFlag\nWhite" to TransgenderFlagWhite,
    "TransgenderFlag\nLightBlue" to TransgenderFlagLightBlue,
    "TransgenderFlag\nMediumBlue" to TransgenderFlagMediumBlue,
    "TransgenderFlag\nDarkBlue" to TransgenderFlagDarkBlue,
    "Success" to Success,
    "SuccessContainer" to SuccessContainer,
    "Warning" to Warning,
    "WarningContainer" to WarningContainer,
)

@Composable
fun ColorPalette(
    colors: Colors,
    modifier: Modifier = Modifier,
    vertical: Boolean = false,
    showLabel: Boolean = false
) {
    val allColors = remember { colors.all() }

    if (vertical) Column(modifier) {
        allColors.forEach {
            BoxWithConstraints(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(maxWidth)
                        .height(maxHeight)
                        .background(it.second)
                ) {
                    if (showLabel) Text(
                        text = it.first,
                        color = Color(0xFF414141),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    } else Row(modifier) {
        allColors.forEach {
            BoxWithConstraints(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(maxWidth)
                        .height(maxHeight)
                        .background(it.second)
                ) {
                    if (showLabel) Text(
                        text = it.first,
                        color = Color(0xFF414141),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Preview(widthDp = 2000)
@Composable
private fun ColorPalettePreview() {
    Column {
        ColorPalette(
            Colors.Light,
            showLabel = true,
            modifier = Modifier
                .width(2000.dp)
                .height(64.dp)
        )
        ColorPalette(
            Colors.Dark,
            showLabel = true,
            modifier = Modifier
                .width(2000.dp)
                .height(64.dp)
        )
    }
}
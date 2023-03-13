package com.vallem.componentlibrary.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

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
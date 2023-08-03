package com.vallem.sylph.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vallem.componentlibrary.ui.theme.TransFlagColors

@Composable
fun ColorSystemBars() {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.run {
            setStatusBarColor(color = TransFlagColors.Pink, darkIcons = true)
            setNavigationBarColor(color = TransFlagColors.Blue, darkIcons = true)
        }
    }
}
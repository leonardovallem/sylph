package com.vallem.componentlibrary.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

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
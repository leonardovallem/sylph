package com.example.sylph.presentation.sylph_onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.sylph.presentation.Routes
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph(start = true)
@Destination(route = Routes.Screen.Onboarding)
@Composable
fun OnboardingScreen() {
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    OnboardingScreen()
}

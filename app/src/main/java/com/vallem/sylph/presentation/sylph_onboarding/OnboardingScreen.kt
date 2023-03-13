package com.vallem.sylph.presentation.sylph_onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import com.vallem.sylph.presentation.Routes
import com.vallem.sylph.presentation.destinations.LoginScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@RootNavGraph(start = true)
@Destination(route = Routes.Screen.Onboarding)
@Composable
fun OnboardingScreen(navigator: DestinationsNavigator) {
    LaunchedEffect(Unit) {
        navigator.navigate(LoginScreenDestination)
    }
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    OnboardingScreen(
        navigator = EmptyDestinationsNavigator
    )
}

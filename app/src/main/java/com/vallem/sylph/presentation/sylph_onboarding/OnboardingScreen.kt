package com.vallem.sylph.presentation.sylph_onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.vallem.sylph.presentation.Routes
import com.vallem.sylph.presentation.destinations.HomeScreenDestination
import com.vallem.sylph.presentation.theme.SylphTheme

@RootNavGraph(start = true)
@Destination(route = Routes.Screen.Onboarding)
@Composable
fun OnboardingScreen(navigator: DestinationsNavigator) {
    Scaffold(modifier = Modifier.fillMaxSize()) { pv ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(pv)
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Onboarding Screen")
            Spacer(modifier = Modifier.weight(1f))

            Button(onClick = { navigator.navigate(HomeScreenDestination) }) {
                Text(text = "Go to Home")
            }
        }
    }
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    SylphTheme {
        OnboardingScreen(
            navigator = EmptyDestinationsNavigator
        )
    }
}

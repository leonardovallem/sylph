package com.vallem.init

import androidx.compose.runtime.Composable
import com.google.firebase.auth.FirebaseAuth
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.vallem.init.destinations.LoginScreenDestination
import com.vallem.sylph.home.presentation.destinations.HomeScreenDestination
import javax.inject.Inject

class SylphNavHost @Inject constructor(private val auth: FirebaseAuth) {
    @Composable
    operator fun invoke(navGraph: NavGraphSpec) {
        DestinationsNavHost(
            navGraph = navGraph,
            startRoute = auth.currentUser?.let {
                HomeScreenDestination
            } ?: LoginScreenDestination
        )
    }
}
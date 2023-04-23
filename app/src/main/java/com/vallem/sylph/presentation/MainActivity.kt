package com.vallem.sylph.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.auth.FirebaseAuth
import com.ramcosta.composedestinations.DestinationsNavHost
import com.vallem.componentlibrary.ui.theme.SylphTheme
import com.vallem.sylph.presentation.destinations.HomeScreenDestination
import com.vallem.sylph.presentation.destinations.LoginScreenDestination
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SylphTheme {
                DestinationsNavHost(
                    navGraph = NavGraphs.root,
                    startRoute = auth.currentUser?.let {
                        HomeScreenDestination
                    } ?: LoginScreenDestination
                )
            }
        }
    }
}

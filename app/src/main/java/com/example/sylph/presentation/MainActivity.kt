package com.example.sylph.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.sylph.presentation.sylph_onboarding.NavGraphs
import com.example.sylph.presentation.theme.SylphTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SylphTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}

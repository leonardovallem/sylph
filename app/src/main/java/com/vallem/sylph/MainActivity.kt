package com.vallem.sylph

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.scope.resultRecipient
import com.vallem.componentlibrary.ui.theme.SylphTheme
import com.vallem.init.SylphNavHost
import com.vallem.sylph.events.presentation.destinations.AnyUserEventDetailsBottomSheetDestination
import com.vallem.sylph.events.presentation.detail.EventDetailsResult
import com.vallem.sylph.home.presentation.HomeScreen
import com.vallem.sylph.home.presentation.destinations.HomeScreenDestination
import com.vallem.sylph.navigation.SylphNavGraph
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var sylphNavHost: SylphNavHost

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SylphTheme {
                sylphNavHost(SylphNavGraph) {
                    composable(HomeScreenDestination) {
                        HomeScreen(
                            navigator = destinationsNavigator,
                            eventDetailsRecipient = resultRecipient<AnyUserEventDetailsBottomSheetDestination, EventDetailsResult>()
                        )
                    }
                }
            }
        }
    }
}

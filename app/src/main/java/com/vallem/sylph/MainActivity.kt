package com.vallem.sylph

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.vallem.componentlibrary.ui.theme.SylphTheme
import com.vallem.init.SylphNavHost
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
                sylphNavHost(SylphNavGraph)
            }
        }
    }
}

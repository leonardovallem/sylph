package com.vallem.init

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.firebase.auth.FirebaseAuth
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.vallem.init.destinations.LoginScreenDestination
import com.vallem.sylph.home.presentation.destinations.HomeScreenDestination
import javax.inject.Inject

class SylphNavHost @Inject constructor(private val auth: FirebaseAuth) {
    @OptIn(
        ExperimentalMaterialNavigationApi::class,
        ExperimentalAnimationApi::class,
        ExperimentalMaterialApi::class
    )
    @Composable
    operator fun invoke(navGraph: NavGraphSpec) {
        val density = LocalDensity.current

        val navController = rememberNavController()
        val bottomSheetState = remember {
            ModalBottomSheetState(
                initialValue = ModalBottomSheetValue.Hidden,
                density = density,
                isSkipHalfExpanded = true
            )
        }
        val bottomSheetNavigator = remember { BottomSheetNavigator(bottomSheetState) }
        navController.navigatorProvider += bottomSheetNavigator
        val navHostEngine = rememberAnimatedNavHostEngine()

        ModalBottomSheetLayout(
            bottomSheetNavigator = bottomSheetNavigator,
            sheetBackgroundColor = Color.Transparent
        ) {
            DestinationsNavHost(
                navGraph = navGraph,
                navController = navController,
                engine = navHostEngine,
                startRoute = auth.currentUser?.let {
                    HomeScreenDestination
                } ?: LoginScreenDestination
            )
        }
    }
}
package com.vallem.sylph.navigation

import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route
import com.vallem.init.destinations.LoginScreenDestination
import com.vallem.init.destinations.OnboardingScreenDestination
import com.vallem.sylph.events.presentation.destinations.AddEventScreenDestination
import com.vallem.sylph.events.presentation.destinations.UserEventsScreenDestination
import com.vallem.sylph.home.presentation.destinations.HomeScreenDestination

object SylphNavGraph : NavGraphSpec {
    override val route: String = "root"

    override val startRoute: Route = OnboardingScreenDestination

    override val destinationsByRoute: Map<String, DestinationSpec<*>> = listOf(
        OnboardingScreenDestination,
        LoginScreenDestination,
        HomeScreenDestination,
        AddEventScreenDestination,
        UserEventsScreenDestination,
    ).associateBy { it.route }
}

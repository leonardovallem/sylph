package com.vallem.sylph.shared

import com.vallem.sylph.shared.map.model.PointWrapper

object Routes {
    object Screen {
        const val Onboarding = "onboarding_screen"
        const val Login = "login_screen"
        const val Register = "register_screen"
        const val Home = "home_screen"
        const val AddEvent = "add_event_screen"
        const val UserEvents = "user_events_screen"
        const val UserDetails = "user_details_screen"
    }

    object BottomSheet {
        const val CurrentUserEventDetails = "current_user_event_detail_bottom_sheet"
        const val AnyUserEventDetails = "any_user_event_detail_bottom_sheet"
    }
}

sealed class SylphDestination(open val route: String) {
    sealed class Screen(override val route: String) : SylphDestination(route) {
        object Onboarding : Screen(Routes.Screen.Onboarding)
        object Login : Screen(Routes.Screen.Login)
        object Home : Screen(Routes.Screen.Home)
        data class AddEvent(val point: PointWrapper?) : Screen(Routes.Screen.AddEvent)
        object UserEvents : Screen(Routes.Screen.UserEvents)
    }
}
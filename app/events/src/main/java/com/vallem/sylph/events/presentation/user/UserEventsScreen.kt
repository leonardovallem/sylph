package com.vallem.sylph.events.presentation.user

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseUser
import com.mapbox.geojson.Point
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.vallem.componentlibrary.domain.model.UserInfo
import com.vallem.componentlibrary.ui.appbar.SylphTopBar
import com.vallem.componentlibrary.ui.button.SylphButton
import com.vallem.sylph.events.domain.EventDetails
import com.vallem.sylph.events.presentation.destinations.EventDetailsBottomSheetDestination
import com.vallem.sylph.events.presentation.user.components.UserEvent
import com.vallem.sylph.shared.Routes
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.model.event.Event
import com.vallem.sylph.shared.domain.model.event.SafetyEvent
import com.vallem.sylph.shared.map.model.PointWrapper
import com.vallem.sylph.shared.presentation.components.FlagLoading
import com.vallem.sylph.shared.presentation.components.NavigationDrawerWrapper
import com.vallem.sylph.shared.presentation.model.NavigationShortcut
import kotlinx.coroutines.launch

@Destination(route = Routes.Screen.UserEvents)
@Composable
fun UserEventsScreen(
    navigator: DestinationsNavigator,
    viewModel: UserEventsViewModel = hiltViewModel()
) {
    UserEventsScreenContent(
        navigator = navigator,
        eventsQueryResult = viewModel.eventsQueryResult,
        currentUser = viewModel.currentUser,
        retryRequest = viewModel::retrieveEvents,
    )
}

@Composable
private fun UserEventsScreenContent(
    navigator: DestinationsNavigator,
    eventsQueryResult: Result<List<Event>>,
    currentUser: FirebaseUser?,
    retryRequest: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    BackHandler(onBack = navigator::navigateUp)

    NavigationDrawerWrapper(
        drawerState = drawerState,
        userInfo = currentUser?.displayName?.let { UserInfo(it, null) },
        navigator = navigator,
        selectedShortcut = NavigationShortcut.RegisteredEvents,
    ) {
        Scaffold(
            topBar = {
                SylphTopBar(
                    title = "Meus eventos",
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch { drawerState.open() }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Menu,
                                contentDescription = "Menu de navegação"
                            )
                        }
                    },
                )
            },
            modifier = Modifier.fillMaxSize(),
        ) { pv ->
            when (eventsQueryResult) {
                is Result.Success -> LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier
                        .padding(pv)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    items(eventsQueryResult.data) {
                        UserEvent(
                            event = it,
                            onClick = {
                                navigator.navigate(EventDetailsBottomSheetDestination(EventDetails.Sync(it))) {
                                    // this avoids bottom sheets to stack in the navigation stack without being cleared
                                    popUpTo(Routes.Screen.UserEvents)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                is Result.Failure -> Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(pv)
                        .padding(24.dp)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ErrorOutline,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(64.dp)
                    )

                    Text(
                        text = eventsQueryResult.e.message ?: "Erro desconhecido",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    SylphButton.Elevated(
                        label = "Tentar novamente",
                        onClick = retryRequest
                    )
                }

                Result.Loading -> Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(pv)
                        .padding(16.dp)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                        .verticalScroll(rememberScrollState())
                ) {
                    repeat(8) {
                        FlagLoading(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .height(80.dp)
                        )
                    }
                }
            }

        }
    }
}

@Preview
@Composable
private fun UserEventsScreenSuccessPreview() {
    UserEventsScreenContent(
        navigator = EmptyDestinationsNavigator,
        eventsQueryResult = Result.Success(
            listOf(
                SafetyEvent(
                    point = PointWrapper(Point.fromLngLat(0.0, 0.0)),
                    reasons = emptySet(),
                    note = "",
                    userId = "",
                    id = null
                )
            )
        ),
        currentUser = null,
        retryRequest = {}
    )
}

@Preview
@Composable
private fun UserEventsScreenLoadingPreview() {
    UserEventsScreenContent(
        navigator = EmptyDestinationsNavigator,
        eventsQueryResult = Result.Loading,
        currentUser = null,
        retryRequest = {}
    )
}
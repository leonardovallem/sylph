package com.vallem.sylph.events.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.SentimentDissatisfied
import androidx.compose.material.icons.rounded.SentimentSatisfied
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.mapbox.geojson.Point
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import com.vallem.componentlibrary.ui.bottomsheet.SylphBottomSheet
import com.vallem.componentlibrary.ui.chip.SylphChip
import com.vallem.componentlibrary.ui.theme.zoneEventColors
import com.vallem.sylph.events.domain.EventDetails
import com.vallem.sylph.events.presentation.components.DoubleRow
import com.vallem.sylph.shared.BuildConfig
import com.vallem.sylph.shared.Routes
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.model.event.DangerEvent
import com.vallem.sylph.shared.domain.model.event.DangerReason
import com.vallem.sylph.shared.domain.model.event.DangerVictim
import com.vallem.sylph.shared.domain.model.event.Event
import com.vallem.sylph.shared.domain.model.event.SafetyEvent
import com.vallem.sylph.shared.domain.model.event.SafetyReason
import com.vallem.sylph.shared.extensions.asGeoIntent
import com.vallem.sylph.shared.map.model.PointWrapper
import com.vallem.sylph.shared.map.presentation.MapLocation

@Destination(
    route = Routes.BottomSheet.EventDetails,
    style = DestinationStyleBottomSheet::class
)
@Composable
fun EventDetailsBottomSheet(
    details: EventDetails,
    navigator: ResultBackNavigator<EventDetailsResult>
) {
    SylphBottomSheet { pv ->
        when (details) {
            is EventDetails.Async -> EventDetailsBottomSheet(
                eventId = details.eventId,
                showUserInfo = details.showUserInfo,
                onShowUserInfo = { navigator.navigateBack(EventDetailsResult.ShowUserDetails(it)) },
                modifier = Modifier.padding(pv)
            )

            is EventDetails.Sync -> EventDetailsBottomSheetBase(
                event = details.event,
                showUserInfo = false
            )
        }
    }
}

@Composable
fun EventDetailsBottomSheet(
    eventId: String,
    showUserInfo: Boolean,
    modifier: Modifier = Modifier,
    onShowUserInfo: (String) -> Unit,
    viewModel: EventDetailsViewModel = hiltViewModel()
) {
    val result by viewModel.result.collectAsState()

    LaunchedEffect(eventId) {
        viewModel.retrieveEventDetails(eventId)
    }

    when (val res = result) {
        is Result.Success -> res.data?.let {
            EventDetailsBottomSheetBase(
                event = it,
                showUserInfo = showUserInfo,
                onUserInfoClick = onShowUserInfo,
            )
        }

        is Result.Failure -> DetailsRetrievalError(modifier = modifier)

        Result.Loading -> DetailsLoading(modifier = modifier)
    }
}

@Composable
private fun EventDetailsBottomSheetBase(
    event: Event,
    showUserInfo: Boolean,
    onUserInfoClick: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val sortedReasons = remember { event.reasons.sortedBy { it.label.length } }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(bottom = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.125f),
                                Color.Black.copy(alpha = 0.25f),
                                Color.Black.copy(alpha = 0.375f),
                                Color.Black.copy(alpha = 0.5f),
                                Color.Black.copy(alpha = 0.625f),
                                Color.Black.copy(alpha = 0.75f),
                            ),
                        )
                    )
                    .size(width = maxWidth, height = maxHeight)
                    .zIndex(1f)
            )

            MapLocation(
                point = event.point.value,
                accessToken = BuildConfig.MAP_BOX_API_TOKEN,
                modifier = Modifier
                    .size(width = maxWidth, height = maxHeight)
                    .zIndex(0f)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .width(maxWidth)
                    .align(Alignment.BottomCenter)
                    .zIndex(2f)
            ) {
                Icon(
                    imageVector = with(Icons.Rounded) {
                        when (event.type) {
                            Event.Type.Safety -> SentimentSatisfied
                            Event.Type.Danger -> SentimentDissatisfied
                        }
                    },
                    tint = with(MaterialTheme.zoneEventColors) {
                        when (event.type) {
                            Event.Type.Safety -> safety
                            Event.Type.Danger -> danger
                        }
                    },
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )

                Text(
                    text = when (event.type) {
                        Event.Type.Safety -> "Local seguro"
                        Event.Type.Danger -> "Local perigoso"
                    },
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC0C0C0),
                    modifier = Modifier.weight(1f)
                )

                FilledTonalIconButton(
                    onClick = { context.startActivity(event.point.value.asGeoIntent()) }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Map,
                        contentDescription = "Abrir no aplicativo de mapas",
                    )
                }
            }
        }

        Text(
            text = "Motivos",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        DoubleRow(
            items = sortedReasons,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            SylphChip.Small(text = it.label)
        }

        if (event.note.isNotBlank()) {
            Text(
                text = "Informações adicionais",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            )

            Text(
                text = event.note,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }

        if (showUserInfo) Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .clickable { onUserInfoClick(event.userId) }
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Ver informações do publicador",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f)
            )

            Icon(imageVector = Icons.Rounded.ChevronRight, contentDescription = null)
        } else Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview("Safety")
@Composable
private fun SafetyEventDetailScreenPreview() {
    SylphBottomSheet {
        EventDetailsBottomSheetBase(
            event = SafetyEvent(
                point = PointWrapper(Point.fromLngLat(0.0, 0.0)),
                reasons = SafetyReason.values.toSet(),
                note = """Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor
                 incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud 
                 exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute 
                 irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla 
                 pariatur."""
                    .replace("\n", "")
                    .replace("  ", " ")
                    .replace("\t", ""),
                userId = "",
                id = null
            ),
            true
        )
    }
}

@Preview("Danger")
@Composable
private fun DangerEventDetailScreenPreview() {
    SylphBottomSheet {
        EventDetailsBottomSheetBase(
            event = DangerEvent(
                point = PointWrapper(Point.fromLngLat(0.0, 0.0)),
                reasons = DangerReason.values.toSet(),
                victim = DangerVictim.OtherPerson,
                note = """Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor
                 incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud 
                 exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute 
                 irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla 
                 pariatur."""
                    .replace("\n", "")
                    .replace("  ", " ")
                    .replace("\t", ""),
                userId = "",
                id = null
            ),
            true
        )
    }
}
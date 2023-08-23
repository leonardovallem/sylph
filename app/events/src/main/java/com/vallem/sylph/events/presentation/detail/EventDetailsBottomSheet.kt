package com.vallem.sylph.events.presentation.detail

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SentimentDissatisfied
import androidx.compose.material.icons.rounded.SentimentSatisfied
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import com.vallem.componentlibrary.ui.bottomsheet.SylphBottomSheet
import com.vallem.componentlibrary.ui.chip.SylphChip
import com.vallem.componentlibrary.ui.theme.zoneEventColors
import com.vallem.sylph.shared.Routes
import com.vallem.sylph.shared.domain.model.event.DangerEvent
import com.vallem.sylph.shared.domain.model.event.DangerReason
import com.vallem.sylph.shared.domain.model.event.DangerVictim
import com.vallem.sylph.shared.domain.model.event.Event
import com.vallem.sylph.shared.domain.model.event.SafetyEvent
import com.vallem.sylph.shared.domain.model.event.SafetyReason
import com.vallem.sylph.shared.map.model.PointWrapper

@Destination(route = Routes.BottomSheet.EventDetails, style = DestinationStyleBottomSheet::class)
@Composable
fun EventDetailsBottomSheet(event: Event) {
    val sortedReasons = remember { event.reasons.sortedBy { it.label.length } }

    SylphBottomSheet(
        nestedScrollState = rememberNestedScrollInteropConnection()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp)
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
                        Event.Type.Safety -> safetySelected
                        Event.Type.Danger -> dangerSelected
                    }
                },
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Text(
                text = when (event.type) {
                    Event.Type.Safety -> "Local seguro"
                    Event.Type.Danger -> "Local perigoso"
                },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(12.dp)
            )

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
        }
    }
}

@Composable
fun <T> DoubleRow(
    items: List<T>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    component: @Composable (T) -> Unit
) {
    val layoutDirection = LocalLayoutDirection.current

    val lists = remember {
        if (items.size % 2 == 0) listOf(
            items.subList(0, items.size / 2),
            items.subList(items.size / 2, items.size)
        ) else listOf(
            items.subList(0, items.size / 2 + 1),
            items.subList(items.size / 2 + 1, items.size)
        )
    }

    Column(
        verticalArrangement = verticalArrangement,
        modifier = modifier
            .width(IntrinsicSize.Min)
            .padding(
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding()
            )
    ) {
        lists.forEach { list ->
            Row(
                horizontalArrangement = horizontalArrangement,
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                Spacer(
                    modifier = Modifier.width(contentPadding.calculateLeftPadding(layoutDirection))
                )

                list.forEach { component(it) }

                Spacer(
                    modifier = Modifier.width(contentPadding.calculateRightPadding(layoutDirection))
                )
            }
        }
    }
}

@Preview("Safety")
@Composable
private fun SafetyEventDetailScreenPreview() {
    EventDetailsBottomSheet(
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
            userId = ""
        ),
    )
}

@Preview("Danger")
@Composable
private fun DangerEventDetailScreenPreview() {
    EventDetailsBottomSheet(
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
            userId = ""
        )
    )
}
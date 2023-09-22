package com.vallem.sylph.events.presentation.user.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SentimentDissatisfied
import androidx.compose.material.icons.rounded.SentimentSatisfied
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.vallem.componentlibrary.ui.chip.SylphChip
import com.vallem.componentlibrary.ui.list.SylphOverflowList
import com.vallem.componentlibrary.ui.theme.SylphTheme
import com.vallem.componentlibrary.ui.theme.zoneEventColors
import com.vallem.sylph.shared.domain.model.event.DangerEvent
import com.vallem.sylph.shared.domain.model.event.DangerReason
import com.vallem.sylph.shared.domain.model.event.DangerVictim
import com.vallem.sylph.shared.domain.model.event.Event
import com.vallem.sylph.shared.domain.model.event.SafetyEvent
import com.vallem.sylph.shared.domain.model.event.SafetyReason
import com.vallem.sylph.shared.extensions.formatAsDMS
import com.vallem.sylph.shared.extensions.truncate
import com.vallem.sylph.shared.map.model.PointWrapper
import com.vallem.sylph.shared.util.rememberGeocoder

@Composable
fun UserEvent(event: Event, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val geocoder = rememberGeocoder()
    val locationDetails = geocoder.briefLocationDetails(event.point.value)

    UserEvent(
        event = event,
        onClick = onClick,
        locationString = locationDetails ?: event.point.value.formatAsDMS,
        modifier = modifier
    )
}

@Composable
private fun UserEvent(
    event: Event,
    onClick: () -> Unit,
    locationString: String,
    modifier: Modifier = Modifier
) {
    val sortedReasons = remember { event.reasons.sortedBy { it.label.length } }

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                with(MaterialTheme.zoneEventColors) {
                    when (event.type) {
                        Event.Type.Safety -> safetyFaded
                        Event.Type.Danger -> dangerFaded
                    }
                }
            )
            .clickable(onClick = onClick)
            .padding(16.dp)
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
            modifier = Modifier.size(48.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            AnimatedContent(targetState = locationString, label = "") {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelLarge,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false,
                    color = when (event.type) {
                        Event.Type.Safety -> MaterialTheme.zoneEventColors.safetySelected
                        Event.Type.Danger -> MaterialTheme.colorScheme.onErrorContainer
                    }
                )
            }

            SylphOverflowList(SylphOverflowList.Orientation.Horizontal) { shownChips ->
                sortedReasons.forEach {
                    SylphChip.Tiny(
                        text = it.label.truncate(15),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }

                if (shownChips < sortedReasons.size) SylphChip.Tiny(text = "${sortedReasons.size - shownChips + 1}+")
            }
        }
    }
}

@Preview
@Composable
private fun UserEventPreview() {
    SylphTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp)
        ) {
            UserEvent(
                event = SafetyEvent(
                    point = PointWrapper(Point.fromLngLat(0.0, 0.0)),
                    reasons = setOf(SafetyReason.FriendlyEstablishmentsAround, SafetyReason.Other),
                    note = "Something",
                    userId = "",
                    id = null
                ),
                locationString = "Av. Afonso Pena, Centro - Belo Horizonte",
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )

            UserEvent(
                event = DangerEvent(
                    point = PointWrapper(Point.fromLngLat(0.0, 0.0)),
                    reasons = DangerReason.values.toSet(),
                    victim = DangerVictim.User,
                    note = "Something",
                    userId = "",
                    id = null
                ),
                locationString = "41°24'12.2\"N 2°10'26.5\"E",
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
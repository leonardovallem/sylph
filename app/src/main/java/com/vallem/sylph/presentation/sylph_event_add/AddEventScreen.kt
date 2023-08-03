package com.vallem.sylph.presentation.sylph_event_add

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Fullscreen
import androidx.compose.material.icons.rounded.FullscreenExit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.mapbox.geojson.Point
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.EmptyResultBackNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.vallem.componentlibrary.ui.appbar.SylphTopBar
import com.vallem.componentlibrary.ui.chip.SylphChip
import com.vallem.componentlibrary.ui.chip.SylphChipDefaults
import com.vallem.componentlibrary.ui.theme.TransFlagColors
import com.vallem.componentlibrary.ui.theme.zoneEventColors
import com.vallem.sylph.BuildConfig
import com.vallem.sylph.map.rememberMapState
import com.vallem.sylph.presentation.Routes
import com.vallem.sylph.presentation.components.MapBox
import com.vallem.sylph.presentation.theme.ColorSystemBars
import com.vallem.sylph.presentation.theme.SylphTheme
import com.vallem.sylph.util.PointWrapper
import com.vallem.sylph.util.truthyCallback

enum class ZoneType {
    Safe, Dangerous
}

enum class SafetyReason(val label: String) {
    WellLit("Bem iluminado"),
    SecurityCameras("Câmeras de segurança"),
    WellGuard("Bem policiado"),
    FriendlyEstablishmentsAround("Estabelecimentos LGTQIAP+"),
    Other("Outra coisa");

    companion object {
        val values = values()
    }
}

enum class DangerReason(val label: String) {
    Battery("Agressão"),
    SexualHarassment("Assédio sexual"),
    MoralHarassment("Assédio moral"),
    Cursing("Xingamentos"),
    Discrimination("Discriminação"),
    Other("Outra coisa");

    companion object {
        val values = values()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Destination(route = Routes.Screen.AddEvent)
@Composable
fun AddEventScreen(point: PointWrapper?, navigator: ResultBackNavigator<Boolean>) {
    val isPreview = LocalInspectionMode.current

    var currentPoint by rememberSaveable { mutableStateOf(point) }
    val mapState = rememberMapState(center = currentPoint?.value)

    var mapExpanded by rememberSaveable { mutableStateOf(false) }
    var zoneType by rememberSaveable { mutableStateOf<ZoneType?>(null) }

    val mapHeightFraction by animateFloatAsState(
        targetValue = if (mapExpanded) 1f else 0.25f,
        label = "MapHeightFraction"
    )

    ColorSystemBars()

    LaunchedEffect(currentPoint) {
        currentPoint?.let {
            mapState.addPointMarker(it.value, true)
        }
    }

    Scaffold(
        topBar = {
            SylphTopBar(
                title = "Adicionar evento",
                navigationIcon = {
                    IconButton(onClick = navigator::navigateBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "Voltar para a Home"
                        )
                    }
                },
            )
        }
    ) { pv ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(pv),
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(mapHeightFraction)
            ) {
                if (isPreview) Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(width = maxWidth, height = maxHeight)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(text = "Map", style = MaterialTheme.typography.titleMedium)
                } else MapBox(
                    state = mapState,
                    accessToken = BuildConfig.MAP_BOX_API_TOKEN,
                    modifier = Modifier.size(width = maxWidth, height = maxHeight),
                    onClick = truthyCallback { currentPoint = PointWrapper(it) }
                )

                IconButton(
                    onClick = { mapExpanded = !mapExpanded },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .zIndex(999f)
                ) {
                    Icon(
                        imageVector = with(Icons.Rounded) {
                            if (mapExpanded) FullscreenExit else Fullscreen
                        },
                        tint = TransFlagColors.Pink,
                        contentDescription = if (mapExpanded) "Reduzir mapa" else "Ampliar mapa"
                    )
                }
            }

            currentPoint?.let { point ->
                EventSettings(
                    point = point.value,
                    zoneType = zoneType,
                    onZoneTypeChange = { zoneType = it }
                )
            }
        }
    }
}

enum class DangerVictim {
    User, OtherPerson
}

sealed class Event(open val point: PointWrapper, open val note: String) {
    data class Safety(
        override val point: PointWrapper,
        val reasons: Set<SafetyReason>,
        override val note: String
    ) : Event(point, note) {
        companion object {
            fun defaultFor(point: Point) = Safety(PointWrapper(point), emptySet(), "")
        }
    }

    data class Danger(
        override val point: PointWrapper,
        val reasons: Set<DangerReason>,
        val victim: DangerVictim?,
        override val note: String
    ) : Event(point, note) {
        companion object {
            fun defaultFor(point: Point) = Danger(PointWrapper(point), emptySet(), null, "")
        }
    }

    fun update(note: String) = when (this) {
        is Safety -> copy(note = note)
        is Danger -> copy(note = note)
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun EventSettings(
    point: Point,
    zoneType: ZoneType?,
    onZoneTypeChange: (ZoneType) -> Unit
) {
    var event by remember { mutableStateOf<Event?>(null) }

    LaunchedEffect(zoneType) {
        event = when (zoneType) {
            ZoneType.Safe -> Event.Safety.defaultFor(point)
            ZoneType.Dangerous -> Event.Danger.defaultFor(point)
            null -> null
        }
    }

    if (BuildConfig.DEBUG) Text(
        text = "${point.latitude()} ${point.longitude()}",
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "A zona selecionada é",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            SylphChip.Primary(
                text = "Segura",
                selected = zoneType == ZoneType.Safe,
                colors = SylphChipDefaults.primaryColors(
                    content = Color.White,
                    container = animateColorAsState(
                        targetValue = MaterialTheme.zoneEventColors.safety,
                        label = "SafeZoneColor"
                    ).value,
                    border = MaterialTheme.zoneEventColors.safetySelected
                ),
                onClick = { onZoneTypeChange(ZoneType.Safe) },
                modifier = Modifier.weight(1f)
            )

            SylphChip.Primary(
                text = "Perigosa",
                selected = zoneType == ZoneType.Dangerous,
                colors = SylphChipDefaults.primaryColors(
                    content = Color.White,
                    container = animateColorAsState(
                        targetValue = MaterialTheme.zoneEventColors.danger,
                        label = "DangerousZoneColor"
                    ).value,
                    border = MaterialTheme.zoneEventColors.dangerSelected
                ),
                onClick = { onZoneTypeChange(ZoneType.Dangerous) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedContent(
            targetState = zoneType,
            label = "ZoneTypeFields",
            transitionSpec = { fadeIn() togetherWith fadeOut() }
        ) { type ->
            type  // TODO find a better way
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                when (val currentEvent = event) {
                    is Event.Safety -> {
                        Text(
                            text = "O que traz segurança à região marcada?",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SafetyReason.values.forEach { reason ->
                                SylphChip.Small(
                                    text = reason.label,
                                    selected = reason in currentEvent.reasons,
                                    onClick = {
                                        event = currentEvent.copy(
                                            reasons = run {
                                                if (reason in currentEvent.reasons) currentEvent.reasons - reason
                                                else currentEvent.reasons + reason
                                            }
                                        )
                                    }
                                )
                            }
                        }
                    }

                    is Event.Danger -> {
                        Text(
                            text = "O que ocorreu na região marcada?",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DangerReason.values.forEach { reason ->
                                SylphChip.Small(
                                    text = reason.label,
                                    selected = reason in currentEvent.reasons,
                                    onClick = {
                                        event = currentEvent.copy(
                                            reasons = run {
                                                if (reason in currentEvent.reasons) currentEvent.reasons - reason
                                                else currentEvent.reasons + reason
                                            }
                                        )
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Quem presenciou/vivenciou isso?",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            SylphChip.Primary(
                                text = "Eu",
                                selected = currentEvent.victim == DangerVictim.User,
                                onClick = { event = currentEvent.copy(victim = DangerVictim.User) },
                                modifier = Modifier.weight(1f)
                            )

                            SylphChip.Primary(
                                text = "Outra pessoa",
                                selected = currentEvent.victim == DangerVictim.OtherPerson,
                                onClick = {
                                    event = currentEvent.copy(victim = DangerVictim.OtherPerson)
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    null -> Unit
                }

                event?.let { currentEvent ->
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Deseja acrescentar mais alguma coisa?",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    TextField(
                        value = currentEvent.note,
                        onValueChange = { event = currentEvent.update(note = it) },
                        placeholder = { Text(text = "Informações adicionais") },
                        maxLines = 5,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AddEventScreenPreview() {
    SylphTheme {
        AddEventScreen(PointWrapper(Point.fromLngLat(50.0, 50.0)), EmptyResultBackNavigator())
    }
}
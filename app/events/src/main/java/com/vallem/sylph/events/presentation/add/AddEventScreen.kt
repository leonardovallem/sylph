package com.vallem.sylph.events.presentation.add

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.mapbox.geojson.Point
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.EmptyResultBackNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.vallem.componentlibrary.ui.appbar.SylphTopBar
import com.vallem.componentlibrary.ui.button.SylphButton
import com.vallem.componentlibrary.ui.chip.SylphChip
import com.vallem.componentlibrary.ui.chip.SylphChipDefaults
import com.vallem.componentlibrary.ui.input.SylphTextField
import com.vallem.componentlibrary.ui.theme.ColorSystemBars
import com.vallem.componentlibrary.ui.theme.SylphTheme
import com.vallem.componentlibrary.ui.theme.TransFlagColors
import com.vallem.componentlibrary.ui.theme.zoneEventColors
import com.vallem.sylph.events.model.DangerReason
import com.vallem.sylph.events.model.DangerVictim
import com.vallem.sylph.events.model.Event
import com.vallem.sylph.events.model.SafetyReason
import com.vallem.sylph.events.model.ZoneType
import com.vallem.sylph.shared.BuildConfig
import com.vallem.sylph.shared.map.model.PointWrapper
import com.vallem.sylph.shared.map.presentation.MapBox
import com.vallem.sylph.shared.map.util.rememberMapState
import com.vallem.sylph.shared.util.truthyCallback

@Destination(route = com.vallem.sylph.shared.Routes.Screen.AddEvent)
@Composable
fun AddEventScreen(
    point: PointWrapper?,
    navigator: ResultBackNavigator<Boolean>
) {
    val isPreview = LocalInspectionMode.current

    var currentPoint by rememberSaveable { mutableStateOf(point) }
    val mapState = rememberMapState(center = currentPoint?.value)

    var mapExpanded by rememberSaveable { mutableStateOf(point == null) }
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
                    onClick = truthyCallback {
                        if (currentPoint == null) mapExpanded = false
                        currentPoint = PointWrapper(it)
                    }
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
                    onZoneTypeChange = { zoneType = it },
                    onConfirm = {
                        // TODO add event to database
                        navigator.navigateBack(result = true)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EventSettings(
    point: Point,
    zoneType: ZoneType?,
    onZoneTypeChange: (ZoneType) -> Unit,
    onConfirm: (Event) -> Unit
) {
    var event by remember { mutableStateOf<Event?>(null) }
    val validForm by remember {
        derivedStateOf {
            when (val currentEvent = event) {
                is Event.Safety -> currentEvent.reasons.isNotEmpty()
                is Event.Danger -> currentEvent.reasons.isNotEmpty() && currentEvent.victim != null
                null -> false
            }
        }
    }

    LaunchedEffect(zoneType) {
        event = when (zoneType) {
            ZoneType.Safe -> Event.Safety.defaultFor(point)
            ZoneType.Dangerous -> Event.Danger.defaultFor(point)
            null -> null
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "A zona selecionada é",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 8.dp)
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

                    SylphTextField.MultiLine(
                        value = currentEvent.note,
                        onValueChange = { event = currentEvent.update(note = it) },
                        placeholder = "Informações adicionais",
                        maxLines = 5,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                SylphButton.Elevated(
                    label = "Salvar",
                    enabled = validForm,
                    onClick = { event?.let(onConfirm) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun AddEventScreenPreview() {
    SylphTheme {
        AddEventScreen(
            PointWrapper(Point.fromLngLat(50.0, 50.0)),
            EmptyResultBackNavigator()
        )
    }
}
package com.vallem.sylph.events.presentation.add

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Fullscreen
import androidx.compose.material.icons.rounded.FullscreenExit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
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
import com.vallem.sylph.shared.BuildConfig
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.model.event.DangerEvent
import com.vallem.sylph.shared.domain.model.event.DangerReason
import com.vallem.sylph.shared.domain.model.event.DangerVictim
import com.vallem.sylph.shared.domain.model.event.Event
import com.vallem.sylph.shared.domain.model.event.SafetyEvent
import com.vallem.sylph.shared.domain.model.event.SafetyReason
import com.vallem.sylph.shared.extensions.getSylphExceptionMessage
import com.vallem.sylph.shared.map.model.PointWrapper
import com.vallem.sylph.shared.map.presentation.MapBox
import com.vallem.sylph.shared.map.util.rememberMapState
import com.vallem.sylph.shared.presentation.components.FlagLoading
import com.vallem.sylph.shared.util.truthyCallback

@Destination(route = com.vallem.sylph.shared.Routes.Screen.AddEvent)
@Composable
fun AddEventScreen(
    initialCameraOptions: CameraOptions?,
    point: PointWrapper?,
    navigator: ResultBackNavigator<Boolean>,
    viewModel: AddEventViewModel = hiltViewModel()
) {
    val isPreview = LocalInspectionMode.current

    val snackbarHostState = remember { SnackbarHostState() }

    var currentPoint by rememberSaveable { mutableStateOf<Point?>(null) }
    val mapState = rememberMapState(center = currentPoint)
    var mapExpanded by rememberSaveable { mutableStateOf(point == null) }

    val mapHeightFraction by animateFloatAsState(
        targetValue = if (mapExpanded) 1f else 0.25f,
        label = "MapHeightFraction"
    )

    ColorSystemBars()

    LaunchedEffect(Unit) {
        initialCameraOptions?.let(mapState::setCameraOptions)
    }

    LaunchedEffect(currentPoint) {
        currentPoint?.let {
            mapState.addPointMarker(it, true)
        }
    }

    LaunchedEffect(viewModel.eventSaveResult) {
        when (val res = viewModel.eventSaveResult) {
            is Result.Success -> {
                navigator.navigateBack(result = true)
                snackbarHostState.showSnackbar(message = "Evento salvo com sucesso!")
            }

            is Result.Failure -> snackbarHostState.showSnackbar(
                message = res.e.getSylphExceptionMessage("Erro ao salvar evento"),
            )

            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                        currentPoint = it
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

            currentPoint?.let {
                EventSettings(
                    point = it,
                    isLoading = viewModel.eventSaveResult == Result.Loading,
                    onConfirm = viewModel::saveEvent
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EventSettings(
    point: Point,
    isLoading: Boolean,
    onConfirm: (Event) -> Unit
) {
    var event by remember { mutableStateOf<Event?>(null) }
    val validForm by remember {
        derivedStateOf {
            when (val currentEvent = event) {
                is SafetyEvent -> currentEvent.reasons.isNotEmpty()
                is DangerEvent -> currentEvent.reasons.isNotEmpty() && currentEvent.victim != null
                else -> false
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
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
                    selected = event is SafetyEvent,
                    colors = SylphChipDefaults.primaryColors(
                        content = Color.White,
                        container = animateColorAsState(
                            targetValue = MaterialTheme.zoneEventColors.safety,
                            label = "SafeZoneColor"
                        ).value,
                        border = MaterialTheme.zoneEventColors.safetySelected
                    ),
                    onClick = { event = SafetyEvent.defaultFor(point) },
                    modifier = Modifier.weight(1f)
                )

                SylphChip.Primary(
                    text = "Perigosa",
                    selected = event is DangerEvent,
                    colors = SylphChipDefaults.primaryColors(
                        content = Color.White,
                        container = animateColorAsState(
                            targetValue = MaterialTheme.zoneEventColors.danger,
                            label = "DangerousZoneColor"
                        ).value,
                        border = MaterialTheme.zoneEventColors.dangerSelected
                    ),
                    onClick = { event = DangerEvent.defaultFor(point) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedContent(
                targetState = event?.type,
                label = "ZoneTypeFields",
                transitionSpec = { fadeIn() togetherWith fadeOut() }
            ) { type ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    when (type) {
                        Event.Type.Safety -> {
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
                                        selected = reason in event?.reasons.orEmpty(),
                                        onClick = {
                                            event = event?.let {
                                                it.update(
                                                    reasons = run {
                                                        if (reason in it.reasons) it.reasons - reason
                                                        else it.reasons + reason
                                                    }
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        }

                        Event.Type.Danger -> {
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
                                        selected = reason in event?.reasons.orEmpty(),
                                        onClick = {
                                            event = event?.let {
                                                it.update(
                                                    reasons = run {
                                                        if (reason in it.reasons) it.reasons - reason
                                                        else it.reasons + reason
                                                    }
                                                )
                                            }
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
                                    selected = (event as? DangerEvent)?.victim == DangerVictim.User,
                                    onClick = {
                                        event = event?.update(victim = DangerVictim.User)
                                    },
                                    modifier = Modifier.weight(1f)
                                )

                                SylphChip.Primary(
                                    text = "Outra pessoa",
                                    selected = (event as? DangerEvent)?.victim == DangerVictim.OtherPerson,
                                    onClick = {
                                        event = event?.update(victim = DangerVictim.OtherPerson)
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        null -> Spacer(modifier = Modifier.fillMaxWidth())
                    }

                    type?.let {
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Deseja acrescentar mais alguma coisa?",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                        SylphTextField.MultiLine(
                            value = event?.note.orEmpty(),
                            onValueChange = { event = event?.update(note = it) },
                            placeholder = "Informações adicionais",
                            maxLines = 5,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        AnimatedVisibility(visible = event != null) {
            AnimatedContent(
                targetState = isLoading,
                label = "ButtonLoadingTransform",
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                if (it) FlagLoading(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .fillMaxWidth()
                        .height(48.dp)
                )
                else SylphButton.Elevated(
                    label = "Salvar",
                    enabled = validForm,
                    onClick = { event?.let(onConfirm) },
                    modifier = Modifier.fillMaxWidth()
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
            null,
            null,
            EmptyResultBackNavigator(),
        )
    }
}
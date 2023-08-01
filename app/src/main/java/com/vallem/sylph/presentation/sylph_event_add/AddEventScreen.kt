package com.vallem.sylph.presentation.sylph_event_add

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.EmptyResultBackNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.vallem.componentlibrary.ui.appbar.SylphTopBar
import com.vallem.componentlibrary.ui.chip.SylphChip
import com.vallem.componentlibrary.ui.chip.SylphChipDefaults
import com.vallem.componentlibrary.ui.theme.TransFlagColors
import com.vallem.componentlibrary.ui.theme.ZoneEventColors
import com.vallem.sylph.BuildConfig
import com.vallem.sylph.map.rememberMapState
import com.vallem.sylph.presentation.Routes
import com.vallem.sylph.presentation.components.MapBox
import com.vallem.sylph.presentation.theme.SylphTheme
import com.vallem.sylph.util.PointWrapper

enum class ZoneType {
    Safe, Dangerous
}

@OptIn(ExperimentalMaterial3Api::class)
@Destination(route = Routes.Screen.AddEvent)
@Composable
fun AddEventScreen(point: PointWrapper?, navigator: ResultBackNavigator<Boolean>) {
    var currentPoint by remember { mutableStateOf(point?.value) }
    val mapState = rememberMapState(center = currentPoint)

    var mapExpanded by remember { mutableStateOf(false) }
    var zoneType by remember { mutableStateOf<ZoneType?>(null) }

    val mapHeightFraction by animateFloatAsState(
        targetValue = if (mapExpanded) 1f else 0.25f,
        label = "MapHeightFraction"
    )

    LaunchedEffect(currentPoint) {
        currentPoint?.let {
            mapState.addPointMarker(it, true)
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
                MapBox(
                    state = mapState,
                    accessToken = BuildConfig.MAP_BOX_API_TOKEN,
                    modifier = Modifier.size(width = maxWidth, height = maxHeight),
                    onClick = {
                        currentPoint = it
                        true
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

            currentPoint?.let { Text(text = "${it.latitude()} ${it.longitude()}") }

            Text(
                text = "A zona selecionada é",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                SylphChip.Primary(
                    text = "Segura",
                    colors = SylphChipDefaults.primaryColors(
                        content = Color.White,
                        container = run {
                            if (zoneType == ZoneType.Safe) ZoneEventColors.SafetySelected
                            else ZoneEventColors.Safety
                        },
                        border = ZoneEventColors.Safety
                    ),
                    onClick = { zoneType = ZoneType.Safe }
                )

                SylphChip.Primary(
                    text = "Perigosa",
                    colors = SylphChipDefaults.primaryColors(
                        content = Color.White,
                        container = run {
                            if (zoneType == ZoneType.Dangerous) ZoneEventColors.DangerSelected
                            else ZoneEventColors.Danger
                        },
                        border = ZoneEventColors.Danger
                    ),
                    onClick = { zoneType = ZoneType.Dangerous }
                )
            }
        }
    }
}

@Preview
@Composable
private fun AddEventScreenPreview() {
    SylphTheme {
        AddEventScreen(null, EmptyResultBackNavigator())
    }
}
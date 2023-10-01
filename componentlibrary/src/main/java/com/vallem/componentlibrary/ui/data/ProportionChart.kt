package com.vallem.componentlibrary.ui.data

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vallem.componentlibrary.ui.theme.SylphTheme
import com.vallem.componentlibrary.ui.theme.zoneEventColors

object ProportionChart {
    data class Entry<T : Number>(val proportion: Float, val label: String, val color: Color, val value: T? = null)

    @Composable
    operator fun <T : Number> invoke(entries: List<Entry<T>>, modifier: Modifier = Modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = modifier
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                entries.forEach {
                    if (it.proportion > 0) ChartLine(
                        color = it.color,
                        value = it.value,
                        modifier = Modifier.weight(it.proportion)
                    )
                }
            }

            entries.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Box(
                        modifier = Modifier
                            .background(it.color, CircleShape)
                            .size(12.dp)
                    )

                    Text(
                        text = it.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun <T : Number> ChartLine(color: Color, modifier: Modifier = Modifier, value: T? = null) {
    if (value == null) Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color, RoundedCornerShape(8.dp))
            .height(12.dp),
    ) else Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(color, RoundedCornerShape(8.dp))
            .padding(2.dp)
    ) {
        Text(text = value.toString(), style = MaterialTheme.typography.labelSmall)
    }
}

@Preview
@Composable
private fun ProportionChartPreview() {
    val entries = listOf(
        ProportionChart.Entry<Int>(
            proportion = 0.65f,
            label = "Eventos aprovados por outros usuários",
            color = MaterialTheme.zoneEventColors.safety,
            value = 65,
        ),
        ProportionChart.Entry(
            proportion = 0.35f,
            label = "Eventos reprovados por outros usuários",
            color = MaterialTheme.zoneEventColors.danger,
            value = 35,
        ),
    )

    SylphTheme {
        ProportionChart(entries)
    }
}
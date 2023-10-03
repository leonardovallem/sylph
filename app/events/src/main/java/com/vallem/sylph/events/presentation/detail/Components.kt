package com.vallem.sylph.events.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ThumbDown
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vallem.componentlibrary.ui.bottomsheet.SylphBottomSheet
import com.vallem.componentlibrary.ui.loading.SylphLoading
import com.vallem.componentlibrary.ui.theme.SylphTheme
import com.vallem.componentlibrary.ui.theme.zoneEventColors
import com.vallem.sylph.shared.domain.model.event.EventVote
import com.vallem.sylph.shared.presentation.components.AlertLevel
import com.vallem.sylph.shared.presentation.components.AlertMessage

@Composable
internal fun DetailsLoading(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        SylphLoading.Circular()
    }
}

@Composable
internal fun DetailsRetrievalError(modifier: Modifier = Modifier) {
    AlertMessage(
        title = "Algo deu errado...",
        description = "Ocorreu um erro ao recuperar os detalhes desse evento.",
        level = AlertLevel.Error,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Preview
@Composable
private fun DetailsLoadingPreview() {
    SylphTheme {
        SylphBottomSheet {
            DetailsLoading(Modifier.padding(it))
        }
    }
}

@Preview
@Composable
private fun DetailsErrorPreview() {
    SylphTheme {
        SylphBottomSheet {
            DetailsRetrievalError(Modifier.padding(it))
        }
    }
}

@Composable
internal fun EventVoteButton(
    userVote: EventVote?,
    buttonVote: EventVote,
    count: Int?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = buttonVote.icon,
                contentDescription = buttonVote.contentDescription,
                tint = buttonVote.colorForUserVote(userVote)
            )
        }

        if (count != null) Text(
            text = count.toString(),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

@Preview
@Composable
private fun EventVoteButtonPreview() {
    var userVote by remember { mutableStateOf<EventVote?>(null) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer),
    ) {
        EventVoteButton(
            userVote = userVote,
            buttonVote = EventVote.DownVote,
            count = 2,
            onClick = { userVote = EventVote.DownVote },
        )

        EventVoteButton(
            userVote = userVote,
            buttonVote = EventVote.UpVote,
            count = 5,
            onClick = { userVote = EventVote.UpVote },
        )
    }
}

private val EventVote.icon: ImageVector
    get() = with(Icons.Rounded) {
        when (this@icon) {
            EventVote.UpVote -> ThumbUp
            EventVote.DownVote -> ThumbDown
        }
    }

private val EventVote.contentDescription: String
    get() = when (this) {
        EventVote.UpVote -> "Aprovar"
        EventVote.DownVote -> "Reprovar"
    }

@Composable
private fun EventVote.colorForUserVote(userVote: EventVote?) = when (this) {
    EventVote.UpVote -> when (userVote) {
        EventVote.UpVote -> MaterialTheme.zoneEventColors.safetySelected
        EventVote.DownVote -> MaterialTheme.colorScheme.onSurfaceVariant
        null -> MaterialTheme.zoneEventColors.safety
    }

    EventVote.DownVote -> when (userVote) {
        EventVote.DownVote -> MaterialTheme.zoneEventColors.dangerSelected
        EventVote.UpVote -> MaterialTheme.colorScheme.onSurfaceVariant
        null -> MaterialTheme.zoneEventColors.danger
    }
}
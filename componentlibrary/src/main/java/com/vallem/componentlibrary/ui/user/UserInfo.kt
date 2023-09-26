package com.vallem.componentlibrary.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun UserInfo(name: String, picUrl: String?, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (picUrl == null) Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = "Sem foto de perfil",
            modifier = Modifier
                .clip(CircleShape)
                .size(64.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(12.dp)
        ) else Box(   // TODO coil image
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .size(64.dp)
        )

        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}

@Preview
@Composable
fun UserInfoSkeleton(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .size(64.dp)
        )

        Box(
            modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .height(24.dp)
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun UserInfoPreview() {
    UserInfo(name = "User Name", picUrl = null)
}
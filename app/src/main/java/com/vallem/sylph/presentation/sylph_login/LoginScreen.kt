package com.vallem.sylph.presentation.sylph_login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.vallem.componentlibrary.ui.classes.forIcons
import com.vallem.componentlibrary.ui.classes.pairWith
import com.vallem.componentlibrary.ui.input.SylphTextField
import com.vallem.sylph.presentation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Destination(route = Routes.Screen.Login)
@Composable
fun LoginScreen(navigator: DestinationsNavigator) {
    val (emailIcon, passwordIcon) = MaterialTheme.colorScheme.run {
        onSurfaceVariant pairWith primary
    }.forIcons(Icons.Outlined.Email, Icons.Outlined.Lock)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) { pv ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(pv)
                .padding(16.dp)
        ) {
            SylphTextField(
                value = "",
                onValueChange = {},
                placeholder = "Email",
                leadingIcon = emailIcon,
                modifier = Modifier.fillMaxWidth()
            )
            SylphTextField(
                value = "",
                onValueChange = {},
                placeholder = "Password",
                leadingIcon = passwordIcon,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        navigator = EmptyDestinationsNavigator
    )
}
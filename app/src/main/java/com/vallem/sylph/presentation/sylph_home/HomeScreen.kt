package com.vallem.sylph.presentation.sylph_home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.vallem.componentlibrary.ui.theme.SylphTheme
import com.vallem.sylph.presentation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Destination(route = Routes.Screen.Home)
@Composable
fun HomeScreen(navigator: DestinationsNavigator, viewModel: HomeViewModel = hiltViewModel()) {
    Scaffold(modifier = Modifier.fillMaxSize()) { pv ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(pv)
                .fillMaxSize()
        ) {
            Text(text = viewModel.currentUser?.displayName.toString())
            Text(text = viewModel.currentUser?.email.toString())
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    SylphTheme {
        HomeScreen(navigator = EmptyDestinationsNavigator)
    }
}
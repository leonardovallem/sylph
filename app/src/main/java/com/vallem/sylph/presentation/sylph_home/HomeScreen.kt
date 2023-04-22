package com.vallem.sylph.presentation.sylph_home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import com.vallem.sylph.presentation.Routes

@Destination(route = Routes.Screen.Home)
@Composable
fun HomeScreen() {
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}
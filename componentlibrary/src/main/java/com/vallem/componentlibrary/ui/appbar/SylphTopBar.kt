package com.vallem.componentlibrary.ui.appbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.vallem.componentlibrary.ui.theme.SylphTheme
import com.vallem.componentlibrary.ui.theme.TransFlagColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SylphTopBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = TransFlagColors.Pink,
            titleContentColor = Color.White
        ),
        navigationIcon = navigationIcon,
        title = { Text(text = title) },
        actions = actions,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun SylphTopBarPreview() {
    SylphTheme {
        SylphTopBar(
            title = "Explorar",
            navigationIcon = {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = "Menu de navegação"
                )
            }
        )
    }
}
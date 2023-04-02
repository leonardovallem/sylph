package com.vallem.componentlibrary.ui.brand

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.vallem.componentlibrary.R
import com.vallem.componentlibrary.ui.theme.SylphTheme

@Composable
fun SylphLogo(
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_sylph_logo),
        contentDescription = "Sylph logo",
        tint = tint,
        modifier = modifier
    )
}

@Preview
@Composable
private fun SylphLogoPreview() {
    SylphTheme {
        SylphLogo()
    }
}
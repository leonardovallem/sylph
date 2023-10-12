package com.vallem.init.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vallem.sylph.init.R

@Composable
fun GoogleSignInButton(
    onClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp,
            pressedElevation = 2.dp,
        )
    ) {
        Text(
            text = "Entrar com Google",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF161616),
        )

        Spacer(modifier = Modifier.width(12.dp))

        AnimatedContent(targetState = isLoading, label = "") {
            if (it) CircularProgressIndicator(modifier = Modifier.size(24.dp))
            else Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_google_logo),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}

@Preview
@Composable
private fun GoogleSignInButtonPreview() {
    GoogleSignInButton({}, false)
}
package com.vallem.componentlibrary.ui.input

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vallem.componentlibrary.ui.classes.SylphIcon
import com.vallem.componentlibrary.ui.classes.pairWith
import com.vallem.componentlibrary.ui.classes.toSylphIcon

@Composable
fun SylphTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: SylphIcon? = null,
    trailingIcon: SylphIcon? = null,
    placeholder: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor by animateColorAsState(
        targetValue = if (isFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
        label = "TextFieldBorderColor",
        animationSpec = tween()
    )

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(2.dp, borderColor, CircleShape)
                .heightIn(min = 56.dp)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            leadingIcon?.let {
                val leadingIconColor by animateColorAsState(
                    targetValue = if (isFocused) it.colors.active else it.colors.inactive,
                    label = "TextFieldLeadingIconColor",
                    animationSpec = tween()
                )

                Icon(
                    imageVector = it.icon,
                    contentDescription = null,
                    tint = leadingIconColor,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(16.dp)
                )
            }

            if (value.isEmpty()) placeholder?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            } else it()

            trailingIcon?.let {
                Icon(imageVector = it.icon, contentDescription = null, tint = it.colors.inactive)
            }
        }
    }
}

@Preview
@Composable
private fun SylphTextFields() {
    var value by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp),
    ) {
        SylphTextField(
            value = value,
            onValueChange = { value = it },
            modifier = Modifier.fillMaxWidth()
        )

        SylphTextField(
            value = value,
            onValueChange = { value = it },
            leadingIcon = Icons.Rounded.Person.toSylphIcon(
                Color.Unspecified pairWith Color.Magenta,
            ),
            placeholder = "User",
            modifier = Modifier.fillMaxWidth()
        )

        SylphTextField(
            value = value,
            onValueChange = { value = it },
            trailingIcon = Icons.Rounded.Clear.toSylphIcon(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
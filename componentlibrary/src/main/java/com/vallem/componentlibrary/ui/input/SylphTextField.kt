package com.vallem.componentlibrary.ui.input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.vallem.componentlibrary.ui.classes.SylphIcon
import com.vallem.componentlibrary.ui.classes.pairWith
import com.vallem.componentlibrary.ui.classes.toSylphIcon
import com.vallem.componentlibrary.ui.theme.SylphTheme

@Composable
fun SylphTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: SylphIcon? = null,
    trailingIcon: SylphIcon? = null,
    placeholder: String? = null,
    helperText: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
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
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        modifier = modifier,
        textStyle = LocalTextStyle.current.merge(TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurfaceVariant)
    ) {
        Column {
            BoxWithConstraints {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(2.dp, borderColor, CircleShape)
                        .width(maxWidth)
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

                    // it() cannot be hidden, so the placeholder is being stacked at the top of it
                    // https://slack-chats.kotlinlang.org/t/9662211/i-have-a-basictextfield-field-which-when-cleaning-up-the-tex
                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) placeholder?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.zIndex(3f)
                            )
                        }

                        it()
                    }

                    trailingIcon?.run {
                        val iconContent = @Composable {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = colors.inactive
                            )
                        }

                        action?.let {
                            IconButton(
                                onClick = it,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .requiredSize(24.dp)
                            ) { iconContent() }
                        } ?: iconContent()
                    }
                }
            }

            AnimatedVisibility(visible = helperText != null, modifier = Modifier.fillMaxWidth()) {
                helperText?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.End,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SylphPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: SylphIcon? = null,
    placeholder: String? = null,
    helperText: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var currentVisualTransformation by remember {
        mutableStateOf<VisualTransformation>(PasswordVisualTransformation())
    }

    SylphTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        leadingIcon = leadingIcon,
        trailingIcon = SylphIcon(
            icon = with(Icons.Rounded) {
                if (currentVisualTransformation is PasswordVisualTransformation) Visibility
                else VisibilityOff
            },
            colors = MaterialTheme.colorScheme.run {
                onSurfaceVariant pairWith onSurfaceVariant
            },
            action = {
                currentVisualTransformation = when (currentVisualTransformation) {
                    is PasswordVisualTransformation -> VisualTransformation.None
                    else -> PasswordVisualTransformation()
                }
            }
        ),
        visualTransformation = currentVisualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        placeholder = placeholder,
        helperText = helperText,
    )
}

@Preview
@Composable
private fun SylphTextFields() {
    var value by remember { mutableStateOf("") }

    SylphTheme {
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
                helperText = "Warning",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
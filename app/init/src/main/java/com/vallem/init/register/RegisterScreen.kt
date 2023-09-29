package com.vallem.init.register

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vallem.componentlibrary.ui.appbar.SylphTopBar
import com.vallem.componentlibrary.ui.button.SylphButton
import com.vallem.componentlibrary.ui.button.SylphButtonDefaults
import com.vallem.componentlibrary.ui.classes.forIcons
import com.vallem.componentlibrary.ui.classes.pairWith
import com.vallem.componentlibrary.ui.input.SylphTextField
import com.vallem.componentlibrary.ui.input.SylphTextFieldState
import com.vallem.componentlibrary.ui.input.errorIf
import com.vallem.componentlibrary.ui.theme.TransFlagColors
import com.vallem.componentlibrary.util.ValidationRule
import com.vallem.sylph.shared.util.getBitmap
import com.vallem.sylph.shared.util.pickImage
import com.vallem.sylph.shared.util.resized
import kotlinx.coroutines.launch

@Preview
@Composable
fun RegisterScreen(viewModel: RegisterViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var pic by remember { mutableStateOf<Bitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult

        scope.launch {
            context.getBitmap(uri)
                ?.resized(300, 300)
                ?.let { pic = it }
        }
    }

    val (nameIcon, emailIcon, passwordIcon) = MaterialTheme.colorScheme.run {
        onSurfaceVariant pairWith primary
    }.forIcons(Icons.Outlined.Person, Icons.Outlined.Email, Icons.Outlined.Lock)

    Scaffold(
        topBar = {
            SylphTopBar(
                title = "Criar conta",
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "Voltar",
                        )
                    }
                }
            )
        },
        bottomBar = {
            SylphButton.Elevated(
                label = "Criar conta",
                onClick = { /*TODO*/ },
                colors = SylphButtonDefaults.elevatedColors(
                    container = TransFlagColors.Blue,
                    content = TransFlagColors.OnBlue
                ),
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            )
        }
    ) { pv ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(pv)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(128.dp)
                    .clickable(
                        role = Role.Button,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = false, radius = 64.dp),
                        onClick = launcher::pickImage,
                    )
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                when (val bitmap = pic) {
                    null -> Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Escolher foto de perfil",
                        modifier = Modifier.size(104.dp)
                    )

                    else -> Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Alterar foto de perfil",
                    )
                }
            }

            SylphTextField.SingleLine(
                value = viewModel.name,
                onValueChange = viewModel::updateName,
                placeholder = "Name",
                leadingIcon = nameIcon,
                state = SylphTextFieldState.errorIf(!viewModel.validName),
                helperText = "Nome não pode ficar em branco".takeIf { !viewModel.validName },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
            )

            SylphTextField.SingleLine(
                value = viewModel.email,
                onValueChange = viewModel::updateEmail,
                placeholder = "Email",
                leadingIcon = emailIcon,
                state = SylphTextFieldState.errorIf(!viewModel.validEmail),
                helperText = "Email inválido".takeIf { !viewModel.validEmail },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
            )

            SylphTextField.Password(
                value = viewModel.password,
                onValueChange = viewModel::updatePassword,
                placeholder = "Password",
                leadingIcon = passwordIcon,
                state = SylphTextFieldState.errorIf(!viewModel.validPassword),
                helperText = ValidationRule.Password.helperTextFor(viewModel.password)
                    .takeIf { !viewModel.validPassword },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                keyboardActions = KeyboardActions(onGo = { /* TODO */ }),
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
            )
        }
    }
}
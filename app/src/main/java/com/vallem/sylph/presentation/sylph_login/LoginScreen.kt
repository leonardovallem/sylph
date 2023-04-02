package com.vallem.sylph.presentation.sylph_login

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.vallem.componentlibrary.ui.brand.SylphLogo
import com.vallem.componentlibrary.ui.button.SylphFilledButton
import com.vallem.componentlibrary.ui.button.SylphTextButton
import com.vallem.componentlibrary.ui.classes.forIcons
import com.vallem.componentlibrary.ui.classes.pairWith
import com.vallem.componentlibrary.ui.input.SylphTextField
import com.vallem.componentlibrary.util.ValidationRule
import com.vallem.sylph.presentation.Routes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Destination(route = Routes.Screen.Login)
@Composable
fun LoginScreen(navigator: DestinationsNavigator, viewModel: LoginViewModel = hiltViewModel()) {
    val (emailIcon, passwordIcon) = MaterialTheme.colorScheme.run {
        onSurfaceVariant pairWith primary
    }.forIcons(Icons.Outlined.Email, Icons.Outlined.Lock)

    val validInput by remember {
        derivedStateOf {
            viewModel.validEmail && viewModel.validPassword
        }
    }

    rememberSystemUiController().setSystemBarsColor(
        color = MaterialTheme.colorScheme.surface,
        darkIcons = !isSystemInDarkTheme()
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) { pv ->
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(pv)
                .padding(16.dp)
        ) {
            SylphLogo()

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                AnimatedContent(
                    targetState = viewModel.isRegister,
                    label = "NameField"
                ) { isRegister ->
                    if (isRegister) SylphTextField(
                        value = viewModel.name,
                        onValueChange = { viewModel.onEvent(LoginEvent.Update.Name(it)) },
                        placeholder = "Name",
                        leadingIcon = emailIcon,
                        helperText = "Nome não pode ficar em branco".takeIf { !viewModel.validName },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        modifier = Modifier
                            .fillMaxWidth()
                            .imePadding()
                    )
                }

                SylphTextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.onEvent(LoginEvent.Update.Email(it)) },
                    placeholder = "Email",
                    leadingIcon = emailIcon,
                    helperText = "Email inválido".takeIf { !viewModel.validEmail },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier
                        .fillMaxWidth()
                        .imePadding()
                )

                SylphTextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.onEvent(LoginEvent.Update.Password(it)) },
                    placeholder = "Password",
                    leadingIcon = passwordIcon,
                    visualTransformation = PasswordVisualTransformation(),
                    helperText = ValidationRule.Password.helperTextFor(viewModel.password)
                        .takeIf { !viewModel.validPassword },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                    keyboardActions = KeyboardActions(onGo = { viewModel.onEvent(LoginEvent.Login) }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .imePadding()
                )
            }

            SylphFilledButton(
                label = if (viewModel.isRegister) "Criar conta" else "Login",
                enabled = validInput,
                onClick = { viewModel.onEvent(LoginEvent.Login) },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )

            AnimatedContent(targetState = viewModel.isRegister, label = "LoginModeSwitch") {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (it) "Já tem uma conta?" else "Ainda não tem uma conta?"
                    )

                    SylphTextButton(
                        label = if (it) "Entrar" else "Criar conta",
                        onClick = { viewModel.onEvent(LoginEvent.SwitchMode) }
                    )
                }
            }
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
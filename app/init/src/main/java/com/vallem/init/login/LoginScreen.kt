package com.vallem.init.login

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.vallem.componentlibrary.ui.brand.SylphLogo
import com.vallem.componentlibrary.ui.button.SylphFilledButton
import com.vallem.componentlibrary.ui.button.SylphTextButton
import com.vallem.componentlibrary.ui.classes.forIcons
import com.vallem.componentlibrary.ui.classes.pairWith
import com.vallem.componentlibrary.ui.input.SylphPasswordField
import com.vallem.componentlibrary.ui.input.SylphTextField
import com.vallem.componentlibrary.ui.input.SylphTextFieldState
import com.vallem.componentlibrary.ui.input.errorIf
import com.vallem.componentlibrary.ui.theme.ColorSystemBars
import com.vallem.componentlibrary.util.ValidationRule
import com.vallem.init.destinations.OnboardingScreenDestination
import com.vallem.sylph.home.presentation.destinations.HomeScreenDestination
import com.vallem.sylph.shared.domain.model.Result

@OptIn(ExperimentalMaterial3Api::class)
@Destination(route = com.vallem.sylph.shared.Routes.Screen.Login)
@Composable
fun LoginScreen(navigator: DestinationsNavigator, viewModel: LoginViewModel = hiltViewModel()) {
    val snackbarHostState = remember { SnackbarHostState() }

    val (nameIcon, emailIcon, passwordIcon) = MaterialTheme.colorScheme.run {
        onSurfaceVariant pairWith primary
    }.forIcons(Icons.Outlined.Person, Icons.Outlined.Email, Icons.Outlined.Lock)

    val isButtonLoading by remember {
        derivedStateOf {
            viewModel.loginResult == Result.Loading || viewModel.signUpResult == Result.Loading
        }
    }

    ColorSystemBars()

    LaunchedEffect(viewModel.loginResult) {
        when (val result = viewModel.loginResult) {
            is Result.Success -> navigator.navigate(HomeScreenDestination)

            is Result.Failure -> snackbarHostState.showSnackbar(
                message = when (result.e) {
                    is FirebaseAuthInvalidCredentialsException -> "Invalid password"
                    is FirebaseAuthInvalidUserException -> "No user found for provided email"

                    else -> result.e.message.toString()
                },
            )

            else -> Unit
        }
    }

    LaunchedEffect(viewModel.signUpResult) {
        when (val result = viewModel.signUpResult) {
            is Result.Success -> navigator.navigate(OnboardingScreenDestination)

            is Result.Failure -> snackbarHostState.showSnackbar(
                message = when (result.e) {
                    is FirebaseAuthUserCollisionException -> {
                        viewModel.onEvent(LoginEvent.SwitchMode)
                        "User already registered. Please login with your account"
                    }

                    else -> result.e.message.toString()
                },
            )

            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                        leadingIcon = nameIcon,
                        state = SylphTextFieldState.errorIf(!viewModel.validName),
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
                    state = SylphTextFieldState.errorIf(!viewModel.validEmail),
                    helperText = "Email inválido".takeIf { !viewModel.validEmail },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier
                        .fillMaxWidth()
                        .imePadding()
                )

                SylphPasswordField(
                    value = viewModel.password,
                    onValueChange = { viewModel.onEvent(LoginEvent.Update.Password(it)) },
                    placeholder = "Password",
                    leadingIcon = passwordIcon,
                    state = SylphTextFieldState.errorIf(viewModel.isRegister && !viewModel.validPassword),
                    helperText = ValidationRule.Password.helperTextFor(viewModel.password)
                        .takeIf { viewModel.isRegister && !viewModel.validPassword },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                    keyboardActions = KeyboardActions(onGo = { viewModel.onEvent(LoginEvent.SignIn) }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .imePadding()
                )
            }

            AnimatedContent(
                targetState = viewModel.loginResult is Result.Loading || viewModel.loginResult is Result.Loading,
                label = "LoginAction"
            ) { isLoading ->
                if (isLoading) Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                ) {
                    CircularProgressIndicator()
                } else Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    SylphFilledButton(
                        label = if (viewModel.isRegister) "Criar conta" else "Login",
                        enabled = viewModel.validInput,
                        isLoading = isButtonLoading,
                        onClick = {
                            viewModel.onEvent(if (viewModel.isRegister) LoginEvent.SignUp else LoginEvent.SignIn)
                        },
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
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        navigator = EmptyDestinationsNavigator
    )
}
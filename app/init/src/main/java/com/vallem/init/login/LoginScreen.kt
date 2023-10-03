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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vallem.componentlibrary.ui.brand.SylphLogo
import com.vallem.componentlibrary.ui.button.SylphButton
import com.vallem.componentlibrary.ui.button.SylphTextButton
import com.vallem.componentlibrary.ui.classes.forIcons
import com.vallem.componentlibrary.ui.classes.pairWith
import com.vallem.componentlibrary.ui.input.SylphTextField
import com.vallem.componentlibrary.ui.input.SylphTextFieldState
import com.vallem.componentlibrary.ui.input.errorIf
import com.vallem.componentlibrary.ui.theme.ColorSystemBars
import com.vallem.init.destinations.RegisterScreenDestination
import com.vallem.sylph.home.presentation.destinations.HomeScreenDestination
import com.vallem.sylph.shared.domain.model.Result

@Destination(route = com.vallem.sylph.shared.Routes.Screen.Login)
@Composable
fun LoginScreen(navigator: DestinationsNavigator, viewModel: LoginViewModel = hiltViewModel()) {
    val snackbarHostState = remember { SnackbarHostState() }
    val formState = with(viewModel) {
        remember(email, validEmail, password, validPassword, validInput) {
            LoginFormState(email, validEmail, password, validPassword, validInput)
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

    LoginScreenContent(
        loginResult = viewModel.loginResult,
        state = formState,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState,
        goToRegisterScreen = { navigator.navigate(RegisterScreenDestination) },
    )
}

@Composable
private fun LoginScreenContent(
    loginResult: Result<FirebaseUser>?,
    state: LoginFormState,
    onEvent: (LoginEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
    goToRegisterScreen: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val (emailIcon, passwordIcon) = MaterialTheme.colorScheme.run {
        onSurfaceVariant pairWith primary
    }.forIcons(Icons.Outlined.Person, Icons.Outlined.Email, Icons.Outlined.Lock)

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
                SylphTextField.SingleLine(
                    value = state.email,
                    onValueChange = { onEvent(LoginEvent.Update.Email(it)) },
                    placeholder = "Email",
                    leadingIcon = emailIcon,
                    state = SylphTextFieldState.errorIf(!state.validEmail),
                    helperText = "Email inválido".takeIf { !state.validEmail },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier
                        .fillMaxWidth()
                        .imePadding()
                )

                SylphTextField.Password(
                    value = state.password,
                    onValueChange = { onEvent(LoginEvent.Update.Password(it)) },
                    placeholder = "Password",
                    leadingIcon = passwordIcon,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                    keyboardActions = KeyboardActions(onGo = { onEvent(LoginEvent.SignIn) }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .imePadding()
                )
            }

            AnimatedContent(
                targetState = loginResult == Result.Loading,
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
                    SylphButton.Pill(
                        label = "Login",
                        enabled = state.validInput,
                        isLoading = loginResult == Result.Loading,
                        onClick = {
                            onEvent(LoginEvent.SignIn)
                            keyboardController?.hide()
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Ainda não tem uma conta?")

                        SylphTextButton(
                            label = "Criar conta",
                            onClick = goToRegisterScreen,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    LoginScreenContent(
        loginResult = null,
        state = LoginFormState("", false, "", false, false),
        onEvent = {},
        snackbarHostState = remember { SnackbarHostState() },
        goToRegisterScreen = {},
    )
}